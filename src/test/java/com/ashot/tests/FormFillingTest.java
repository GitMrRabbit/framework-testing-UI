package com.ashot.tests;

import com.ashot.base.BaseTest;
import com.ashot.base.Stabilization;
import com.ashot.pojo.PracticeFormPage;
import com.ashot.utils.ScreenshotComparisonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junitpioneer.jupiter.RetryingTest;
import ru.yandex.qatools.ashot.Screenshot;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class FormFillingTest extends BaseTest implements Stabilization {

    @RetryingTest(maxAttempts = 2)
    @SneakyThrows
    public void testCompleteFormFillingScenario() {
        String url = config.baseUrl() + "/automation-practice-form";
        driver.get(url);
        PracticeFormPage formPage = new PracticeFormPage(driver, config.timeout());
        formPage.waitForPageLoad();
        stabilization(3000);
        Screenshot emptyFormScreenshot = takeStableScreenshot();
        boolean emptyFormHasDiff = ScreenshotComparisonUtils.compareAndAttach(
                screenshotUtils,
                emptyFormScreenshot,
                "empty_form_current",
                "prod_form_scenario_start",
                "Empty Form"
        );
        formPage.fillCompleteForm(
                "Ivan", "Ivanov", "ivan.ivanov@test.com", "1234567890",
                "1990", "0", "01", "Maths", "ул. Ленина, д. 1, кв. 1", "NCR", "Delhi"
        );
        Screenshot filledFormScreenshot = takeStableScreenshot();
        boolean filledFormHasDiff = ScreenshotComparisonUtils.compareAndAttach(
                screenshotUtils,
                filledFormScreenshot,
                "filled_form_current",
                "prod_form_scenario_filled",
                "Filled Form"
        );
        formPage.submitForm();
        formPage.isModalVisible();
        assertTrue(formPage.isModalVisible(), "Модальное окно с результатами должно отображаться");
        Screenshot submittedFormScreenshot = takeStableScreenshot();
        boolean submittedFormHasDiff = ScreenshotComparisonUtils.compareAndAttach(
                screenshotUtils,
                submittedFormScreenshot,
                "submitted_form_current",
                "prod_form_scenario_submitted",
                "Submitted Form"
        );
        assertFalse(emptyFormHasDiff, "Визуальное сравнение пустой формы не прошло");
        assertFalse(filledFormHasDiff, "Визуальное сравнение заполненной формы не прошло");
        assertFalse(submittedFormHasDiff, "Визуальное сравнение отправленной формы не прошло");
    }

    @RetryingTest(maxAttempts = 2)
    @SneakyThrows
    public void testCompleteFormFillingScenarioFailing() {
        // перепутанный baseline для провала
        String url = config.baseUrl() + "/automation-practice-form";
        driver.get(url);
        PracticeFormPage formPage = new PracticeFormPage(driver, config.timeout());
        formPage.waitForPageLoad();
        stabilization(3000);
        Screenshot emptyFormScreenshot = takeStableScreenshot();
        screenshotUtils.saveScreenshot(emptyFormScreenshot, "empty_form_current_fail");
        boolean emptyFormHasDiff = ScreenshotComparisonUtils.compareAndAttach(
                screenshotUtils,
                emptyFormScreenshot,
                "empty_form_current_fail",
                "prod_form_scenario_filled",
                "Empty Form Failing"
        );
        formPage.fillCompleteForm(
                "Ivan", "Ivanov", "ivan.ivanov@test.com", "1234567890",
                "1990", "0", "01", "Maths", "ул. Ленина, д. 1, кв. 1", "NCR", "Delhi"
        );
        Screenshot filledFormScreenshot = takeStableScreenshot();
        screenshotUtils.saveScreenshot(filledFormScreenshot, "filled_form_current_fail");
        boolean filledFormHasDiff = ScreenshotComparisonUtils.compareAndAttach(
                screenshotUtils,
                filledFormScreenshot,
                "filled_form_current_fail",
                "prod_form_scenario_submitted",
                "Filled Form Failing"
        );
        formPage.submitForm();
        formPage.isModalVisible();
        Screenshot submittedFormScreenshot = takeStableScreenshot();
        screenshotUtils.saveScreenshot(submittedFormScreenshot, "submitted_form_current_fail");
        boolean submittedFormHasDiff = ScreenshotComparisonUtils.compareAndAttach(
                screenshotUtils,
                submittedFormScreenshot,
                "submitted_form_current_fail",
                "prod_form_scenario_start",
                "Submitted Form Failing"
        );
        assertFalse(emptyFormHasDiff, "Визуальное сравнение пустой формы не прошло (заведомо провальный тест)");
        assertFalse(filledFormHasDiff, "Визуальное сравнение заполненной формы не прошло (заведомо провальный тест)");
        assertFalse(submittedFormHasDiff, "Визуальное сравнение отправленной формы не прошло (заведомо провальный тест)");
    }
}
