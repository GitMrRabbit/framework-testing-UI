package com.ashot.utils;

import com.ashot.annotations.Stabilize;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.cropper.DefaultCropper;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Thread.sleep;

@Slf4j
public class ScreenshotUtils {

    private final WebDriver driver;
    private final String screenshotDir;
    private final String baselineDir;
    private final String diffDir;
    private static final int DIFF_SIZE_THRESHOLD = 15000;
    private static final double DIFF_PERCENTAGE_THRESHOLD = 2.1;

    public ScreenshotUtils(WebDriver driver, String screenshotDir, String baselineDir, String diffDir) {
        this.driver = driver;
        this.screenshotDir = screenshotDir;
        this.baselineDir = baselineDir;
        this.diffDir = diffDir;
        createDirectories();
    }

    private void createDirectories() {
        try {
            for (String dir : new String[]{screenshotDir, baselineDir, diffDir}) {
                Path path = Paths.get(dir);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                    log.info("Создана директория: {}", dir);
                }
            }
        } catch (IOException e) {
            log.error("Ошибка создания директорий", e);
        }
    }

    @Stabilize
    @SneakyThrows
    public Screenshot takeScreenshot() {
        driver.manage().window().setSize(new Dimension(2560, 1440));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        sleep(1000);
        PageStabilizationUtils stabilizationUtils = new PageStabilizationUtils(driver, 5000);
        stabilizationUtils.disableAnimations();
        stabilizationUtils.waitForPageToBeStable();
        stabilizationUtils.waitForStableViewport();
        sleep(2000);
        Long pageHeight = (Long) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
        log.info("Полная высота страницы: {}px", pageHeight);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        sleep(1000);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        sleep(1000);
        log.info("Снимаем полный скриншот всей страницы (viewport pasting с полной высотой {})", pageHeight);
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(2000))
                .imageCropper(new DefaultCropper())
                .takeScreenshot(driver);

        log.info("Размер скриншота: {}x{} (ожидаемая высота: {})",
                screenshot.getImage().getWidth(), screenshot.getImage().getHeight(), pageHeight);
        if (screenshot.getImage().getHeight() < pageHeight - 100) { // допускаем небольшую погрешность
            log.warn("Скриншот может быть неполным! Высота скриншота: {}, высота страницы: {}",
                    screenshot.getImage().getHeight(), pageHeight);
        } else {
            log.info("Скриншот покрывает всю страницу");
        }

        return screenshot;
    }

    @Stabilize
    public Screenshot takeScreenshotOfElement(org.openqa.selenium.WebElement element) {
        PageStabilizationUtils stabilizer = new PageStabilizationUtils(driver, 5000);
        stabilizer.stabilizePage();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(2000))
                .takeScreenshot(driver, element);
    }

    public void saveScreenshot(Screenshot screenshot, String fileName) {
        try {
            String fullPath = screenshotDir + File.separator + fileName + ".png";
            File file = new File(fullPath);
            ImageIO.write(screenshot.getImage(), "PNG", file);
            log.info("Скриншот сохранен: {}", fullPath);
        } catch (IOException e) {
            log.error("Ошибка сохранения скриншота: {}", fileName, e);
        }
    }

    public void saveBaselineScreenshot(Screenshot screenshot, String fileName) {
        try {
            String fullPath = baselineDir + File.separator + fileName + ".png";
            File file = new File(fullPath);
            ImageIO.write(screenshot.getImage(), "PNG", file);
            log.info("Эталонный скриншот сохранен: {}", fullPath);
        } catch (IOException e) {
            log.error("Ошибка сохранения эталонного скриншота: {}", fileName, e);
        }
    }

    public ImageDiff compareScreenshots(Screenshot actual, Screenshot expected) {
        ImageDiffer imageDiffer = new ImageDiffer().withColorDistortion(50);
        ImageDiff diff = imageDiffer.makeDiff(expected, actual);

        if (diff.hasDiff() && diff.getDiffSize() < DIFF_SIZE_THRESHOLD) {
            log.debug("Различия слишком малы ({} пикселей), игнорируем", diff.getDiffSize());
            return imageDiffer.makeDiff(actual, actual);
        }

        if (diff.hasDiff()) {
            BufferedImage markedImage = diff.getMarkedImage();
            if (markedImage != null) {
                highlightDifferences(markedImage);
            }
        }

        return diff;
    }

    public boolean hasSignificantDiff(ImageDiff diff) {
        if (!diff.hasDiff()) {
            return false;
        }

        if (diff.getDiffSize() >= DIFF_SIZE_THRESHOLD) {
            return true;
        }

        try {
            BufferedImage diffImage = diff.getMarkedImage();
            if (diffImage != null) {
                int totalPixels = diffImage.getWidth() * diffImage.getHeight();
                double diffPercentage = (double) diff.getDiffSize() / totalPixels;
                return diffPercentage >= DIFF_PERCENTAGE_THRESHOLD;
            }
        } catch (Exception e) {
            log.debug("Не удалось рассчитать процент различий", e);
        }

        return false;
    }

    private void highlightDifferences(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                if (red > 200 && green < 100 && blue < 100) {
                    int newRgb = (200 << 24) | (0 << 16) | (120 << 8) | 255;
                    image.setRGB(x, y, newRgb);
                }
            }
        }
    }

    public void saveDiff(ImageDiff diff, String fileName) {
        if (!diff.hasDiff()) {
            log.info("Нет различий для сохранения: {}", fileName);
            return;
        }

        try {
            BufferedImage diffImage = diff.getMarkedImage();
            if (diffImage != null) {
                highlightDifferences(diffImage);

                String fullPath = diffDir + File.separator + fileName + ".png";
                File file = new File(fullPath);
                ImageIO.write(diffImage, "PNG", file);
                log.info("Diff изображение сохранено: {}", fullPath);
                log.info("Размер отличий: {} пикселей", diff.getDiffSize());
            } else {
                log.warn("Изображение diff равно null для: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Ошибка сохранения diff изображения: {}", fileName, e);
        }
    }

    public Screenshot loadScreenshot(String fileName) throws IOException {
        String fullPath = baselineDir + File.separator + fileName + ".png";
        File file = new File(fullPath);

        if (!file.exists()) {
            log.warn("Скриншот не найден: {}", fullPath);
            throw new IOException("Скриншот не найден: " + fullPath);
        }

        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            throw new IOException("Не удалось загрузить изображение: " + fullPath);
        }

        return new Screenshot(image);
    }

    public boolean baselineExists(String fileName) {
        String fullPath = baselineDir + File.separator + fileName + ".png";
        File file = new File(fullPath);
        return file.exists();
    }

    public void createBaselineManually(String testName) {
        Screenshot screenshot = takeScreenshot();
        saveBaselineScreenshot(screenshot, testName);
        log.info("Manual baseline created: {}", testName);
    }
}
