package com.ashot.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public class PageStabilizationUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PageStabilizationUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public void disableAnimations() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "var style = document.createElement('style');" +
                "style.innerHTML = '* { animation: none !important; transition: none !important; }';" +
                "document.head.appendChild(style);"
            );
            log.debug("Анимации отключены");
        } catch (Exception e) {
            log.warn("Не удалось отключить анимации: {}", e.getMessage());
        }
    }

    public void waitForPageToBeStable() {
        wait.until(driver -> {
            try {
                return ((JavascriptExecutor) driver).executeScript(
                    "return document.readyState === 'complete' && " +
                    "(typeof jQuery === 'undefined' || jQuery.active === 0)"
                ).equals(true);
            } catch (Exception e) {
                return false;
            }
        });
        log.debug("Страница стабилизирована");
    }

    public void waitForResourcesToLoad() {
        wait.until(driver -> {
            try {
                Long pendingRequests = (Long) ((JavascriptExecutor) driver).executeScript(
                    "return window.performance.getEntriesByType('resource')" +
                    ".filter(r => !r.transferSize && r.transferSize !== 0).length"
                );
                return pendingRequests == 0;
            } catch (Exception e) {
                return true;
            }
        });
        log.debug("Ресурсы страницы загружены");
    }

    public void waitForStableViewport() {
        String previousViewport = "";
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
                String currentViewport = (String) ((JavascriptExecutor) driver).executeScript(
                    "return window.innerWidth + ',' + window.innerHeight + ',' + window.pageYOffset"
                );
                if (currentViewport.equals(previousViewport) && !previousViewport.isEmpty()) {
                    log.debug("Viewport стабилизирован");
                    return;
                }
                previousViewport = currentViewport;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.debug("Viewport стабилизирован после нескольких проверок");
    }

    public void stabilizePage() {
        disableAnimations();
        waitForPageToBeStable();
        waitForResourcesToLoad();
        waitForStableViewport();
        log.info("Страница полностью стабилизирована");
    }
}
