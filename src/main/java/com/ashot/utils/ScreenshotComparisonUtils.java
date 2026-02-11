package com.ashot.utils;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;

import java.io.IOException;

@Slf4j
public class ScreenshotComparisonUtils {

    public static boolean compareAndAttach(ScreenshotUtils screenshotUtils, Screenshot current, String currentName, String baselineName, String testName) {
        try {
            Screenshot baseline = screenshotUtils.loadScreenshot(baselineName);
            ImageDiff diff = screenshotUtils.compareScreenshots(current, baseline);
            boolean hasDiff = screenshotUtils.hasSignificantDiff(diff);

            if (hasDiff) {
                screenshotUtils.saveDiff(diff, currentName + "_diff");
                Allure.addAttachment("Baseline Screenshot", "image/png", ScreenshotAttachmentUtils.getBaselineScreenshot(baselineName), "png");
                Allure.addAttachment("Current Screenshot", "image/png", ScreenshotAttachmentUtils.getCurrentScreenshot(currentName), "png");
                Allure.addAttachment("Diff Screenshot", "image/png", ScreenshotAttachmentUtils.getDiffScreenshot(currentName + "_diff"), "png");
                log.error("{} отличается от эталона! Различий: {} пикселей", testName, diff.getDiffSize());
            } else {
                log.info("{} соответствует эталону", testName);
            }

            return hasDiff;
        } catch (IOException e) {
            log.error("Ошибка при сравнении скриншотов для {}: {}", testName, e.getMessage());
            return true;
        }
    }
}
