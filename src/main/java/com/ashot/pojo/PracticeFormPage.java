package com.ashot.pojo;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


@Slf4j
public class PracticeFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstNameInput = By.id("firstName");
    private final By lastNameInput = By.id("lastName");
    private final By emailInput = By.id("userEmail");
    private final By genderMaleRadio = By.cssSelector("label[for='gender-radio-1']");
    private final By phoneInput = By.id("userNumber");
    private final By dateOfBirthInput = By.id("dateOfBirthInput");
    private final By subjectsInput = By.id("subjectsInput");
    private final By hobbiesSportsCheckbox = By.cssSelector("label[for='hobbies-checkbox-1']");
    private final By currentAddressTextarea = By.id("currentAddress");
    private final By stateSelect = By.id("state");
    private final By citySelect = By.id("city");
    private final By submitButton = By.id("submit");
    private final By modalContent = By.className("modal-content");

    public PracticeFormPage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    @Step("Ожидание загрузки страницы Practice Form")
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        log.info("Страница Practice Form загружена");
    }

    @Step("Ввод имени: {firstName}")
    public void enterFirstName(String firstName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstNameInput));
        element.clear();
        element.sendKeys(firstName);
        log.info("Введено имя: {}", firstName);
    }

    @Step("Ввод фамилии: {lastName}")
    public void enterLastName(String lastName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(lastNameInput));
        element.clear();
        element.sendKeys(lastName);
        log.info("Введена фамилия: {}", lastName);
    }

    @Step("Ввод email: {email}")
    public void enterEmail(String email) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(emailInput));
        element.clear();
        element.sendKeys(email);
        log.info("Введен email: {}", email);
    }

    @Step("Выбор мужского пола")
    public void selectMaleGender() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(genderMaleRadio));
        element.click();
        log.info("Выбран пол: мужской");
    }

    @Step("Ввод номера телефона: {phone}")
    public void enterPhoneNumber(String phone) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(phoneInput));
        element.clear();
        element.sendKeys(phone);
        log.info("Введен номер телефона: {}", phone);
    }

    @Step("Выбор даты рождения: {day}.{month}.{year}")
    public void selectDateOfBirth(String year, String month, String day) {
        WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthInput));
        scrollToElement(dateInput);
        dateInput.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker__month-container")));

        WebElement yearSelect = wait.until(ExpectedConditions.elementToBeClickable(By.className("react-datepicker__year-select")));
        yearSelect.click();
        WebElement yearOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[@value='" + year + "']")));
        yearOption.click();

        WebElement monthSelect = wait.until(ExpectedConditions.elementToBeClickable(By.className("react-datepicker__month-select")));
        monthSelect.click();
        WebElement monthOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[@value='" + month + "']")));
        monthOption.click();

        WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'react-datepicker__day--0" + day + "') and not(contains(@class,'react-datepicker__day--outside-month'))]")));
        scrollToElement(dayElement);
        dayElement.click();

        log.info("Выбрана дата рождения: {}.{}.{}", day, month, year);
    }

    @Step("Ввод предмета: {subject}")
    public void enterSubject(String subject) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(subjectsInput));
        element.sendKeys(subject);
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-select-2-option-0")));
        option.click();
        log.info("Выбран предмет: {}", subject);
    }

    @Step("Выбор хобби: Sports")
    public void selectSportsHobby() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(hobbiesSportsCheckbox));
        element.click();
        log.info("Выбрано хобби: Sports");
    }

    @Step("Ввод адреса: {address}")
    public void enterCurrentAddress(String address) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(currentAddressTextarea));
        element.clear();
        element.sendKeys(address);
        log.info("Введен адрес: {}", address);
    }

    @Step("Выбор штата: {state}")
    public void selectState(String state) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(stateSelect));
        scrollToElement(element);
        element.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-select-3-option-0")));
        scrollToElement(option);
        option.click();
        log.info("Выбран штат: {}", state);
    }

    public void selectCity(String city) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(citySelect));
        element.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-select-4-option-0")));
        option.click();
        log.info("Выбран город: {}", city);
    }

    private void scrollToElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    @Step("Отправка формы")
    public void submitForm() {
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        scrollToElement(submitBtn);
        submitBtn.click();
        log.info("Форма отправлена");
    }

    @Step("Проверка отображения модального окна с результатами")
    public boolean isModalVisible() {
        try {
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));
            return modal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Заполнение полной формы: {firstName} {lastName}, {email}, {phone}, {birthYear}-{birthMonth}-{birthDay}, {subject}, {address}, {state}, {city}")
    public void fillCompleteForm(String firstName, String lastName, String email, String phone,
                                 String birthYear, String birthMonth, String birthDay,
                                 String subject, String address, String state, String city) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        selectMaleGender();
        enterPhoneNumber(phone);
        selectDateOfBirth(birthYear, birthMonth, birthDay);
        enterSubject(subject);
        selectSportsHobby();
        enterCurrentAddress(address);
        selectState(state);
        selectCity(city);
    }
}
