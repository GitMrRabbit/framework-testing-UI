package com.ashot.base;

import com.ashot.config.TestConfig;
import com.ashot.utils.PageStabilizationUtils;
import com.ashot.utils.ScreenshotUtils;
import com.ashot.utils.WebDriverUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import ru.yandex.qatools.ashot.Screenshot;

import java.time.Duration;

@Slf4j
public abstract class BaseTest {

    protected WebDriver driver;
    protected TestConfig config;
    protected ScreenshotUtils screenshotUtils;
    protected WebDriverUtils webDriverUtils;
    protected PageStabilizationUtils stabilizer;

    protected void initStabilizer() {
        if (driver != null && stabilizer == null) {
            stabilizer = new PageStabilizationUtils(driver, 5000);
        }
    }

    protected Screenshot takeStableScreenshot() {
        if (stabilizer == null) {
            initStabilizer();
        }

        if (stabilizer != null) {
            stabilizer.disableAnimations();
            stabilizer.waitForPageToBeStable();
            stabilizer.waitForStableViewport();
        }

        return screenshotUtils.takeScreenshot();
    }

    protected void saveStableScreenshot(String name) {
        Screenshot screenshot = takeStableScreenshot();
        screenshotUtils.saveScreenshot(screenshot, name);
    }

    @BeforeEach
    public void setUp() {
        config = ConfigFactory.create(TestConfig.class);
        driver = createWebDriver();
        driver.manage().window().setSize(new Dimension(2560, 1440));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        webDriverUtils = new WebDriverUtils(driver, config.timeout());
        screenshotUtils = new ScreenshotUtils(driver, config.screenshotDir(), config.baselineDir(), config.diffDir());
    }

    private WebDriver createWebDriver() {
        WebDriver webDriver;
        switch (config.browser().toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (config.headless()) {
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--window-size=2560,1440");
                }
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-plugins");
                chromeOptions.addArguments("--disable-images");
                webDriver = new ChromeDriver(chromeOptions);
                if (!config.headless()) {
                    webDriver.manage().window().setSize(new Dimension(2560, 1440));
                }
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (config.headless()) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=2560");
                    firefoxOptions.addArguments("--height=1440");
                }
                webDriver = new FirefoxDriver(firefoxOptions);
                if (!config.headless()) {
                    webDriver.manage().window().setSize(new Dimension(2560, 1440));
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + config.browser());
        }
        return webDriver;
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                log.error("Ошибка при закрытии браузера", e);
            }
        }
    }
}
