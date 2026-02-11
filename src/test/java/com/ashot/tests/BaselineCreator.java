package com.ashot.tests;

import com.ashot.base.BaselineBaseTest;
import com.ashot.base.Stabilization;
import com.ashot.pojo.PracticeFormPage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.yandex.qatools.ashot.Screenshot;

@Slf4j
public class BaselineCreator extends BaselineBaseTest implements Stabilization {

    @Test
    @SneakyThrows
    public void createEmptyFormBaseline() {
        String url = baselineConfig.prodUrl() + "/automation-practice-form";
        driver.get(url);
        PracticeFormPage formPage = new PracticeFormPage(driver, baselineConfig.timeout());
        formPage.waitForPageLoad();
        stabilization(3000);
        Screenshot screenshot = takeStableScreenshot();
        screenshotUtils.saveBaselineScreenshot(screenshot, "prod_form_scenario_start");
        log.info("Создан эталонный скриншот пустой формы: prod_form_scenario_start");
    }

    @Test
    @SneakyThrows
    public void createFilledFormBaseline() {
        String url = baselineConfig.prodUrl() + "/automation-practice-form";
        driver.get(url);
        PracticeFormPage formPage = new PracticeFormPage(driver, baselineConfig.timeout());
        formPage.waitForPageLoad();
        formPage.fillCompleteForm(
                "Ivan", "Ivanov", "ivan.ivanov@test.com", "1234567890",
                "1990", "0", "01", "Maths", "ул. Ленина, д. 1, кв. 1", "NCR", "Delhi"
        );
        Screenshot screenshot = takeStableScreenshot();
        screenshotUtils.saveBaselineScreenshot(screenshot, "prod_form_scenario_filled");
        log.info("Создан эталонный скриншот заполненной формы: prod_form_scenario_filled");
    }

    @Test
    @SneakyThrows
    public void createSubmittedFormBaseline() {
        String url = baselineConfig.prodUrl() + "/automation-practice-form";
        driver.get(url);
        PracticeFormPage formPage = new PracticeFormPage(driver, baselineConfig.timeout());
        formPage.waitForPageLoad();
        formPage.fillCompleteForm(
                "Ivan", "Ivanov", "ivan.ivanov@test.com", "1234567890",
                "1990", "0", "01", "Maths", "ул. Ленина, д. 1, кв. 1", "NCR", "Delhi"
        );
        stabilization(3000);
        formPage.submitForm();
        formPage.isModalVisible();
        Screenshot screenshot = takeStableScreenshot();
        screenshotUtils.saveBaselineScreenshot(screenshot, "prod_form_scenario_submitted");
        log.info("Создан эталонный скриншот отправленной формы: prod_form_scenario_submitted");
    }
}