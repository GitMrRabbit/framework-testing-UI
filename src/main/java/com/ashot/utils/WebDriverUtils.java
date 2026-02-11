package com.ashot.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

@Slf4j
public class WebDriverUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WebDriverUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebElement waitForElementVisible(By locator) {
        return retry(() -> wait.until(ExpectedConditions.visibilityOfElementLocated(locator)),
                "Element not visible: " + locator);
    }

    public WebElement waitForElementClickable(By locator) {
        return retry(() -> wait.until(ExpectedConditions.elementToBeClickable(locator)),
                "Element not clickable: " + locator);
    }

    private <T> T retry(java.util.function.Supplier<T> action, String errorMessage) {
        int attempts = 3;
        while (attempts-- > 0) {
            try {
                return action.get();
            } catch (Exception e) {
                if (attempts == 0) {
                    log.error(errorMessage, e);
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException(errorMessage);
    }

    public void waitForPageLoad() {
        wait.until(driver -> ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void typeText(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clickElement(By locator) {
        WebElement element = waitForElementClickable(locator);
        element.click();
    }

    public String getElementText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    public String getElementAttribute(By locator, String attributeName) {
        return waitForElementVisible(locator).getAttribute(attributeName);
    }

    public Object executeJavaScript(String script, Object... args) {
        return ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script, args);
    }

    private <T> T retry(Function<Void, T> action, String errorMessage) {
        int attempts = 3;
        while (attempts-- > 0) {
            try {
                return action.apply(null);
            } catch (Exception e) {
                if (attempts == 0) {
                    log.error(errorMessage, e);
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException(errorMessage);
    }
}
