package core.pages;

import core.data.SignupData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountInformationPage extends BasePage {

    private static final By TITLE_GENDER_MR = By.cssSelector("#id_gender1");
    private static final By TITLE_GENDER_MRS = By.cssSelector("#id_gender2");
    private static final By NAME = By.cssSelector("input[data-qa=\"name\"]");
    private static final By EMAIL = By.cssSelector("input[data-qa=\"email\"]");
    private static final By PASSWORD = By.cssSelector("input[data-qa=\"password\"]");
    private static final By DAY = By.cssSelector("select[data-qa=\"days\"]");
    private static final By MONTH = By.cssSelector("select[data-qa=\"months\"]");
    private static final By YEAR = By.cssSelector("select[data-qa=\"years\"]");
    private static final By FIRST_NAME = By.cssSelector("input[data-qa=\"first_name\"]");
    private static final By LAST_NAME = By.cssSelector("input[data-qa=\"last_name\"]");
    private static final By COMPANY = By.cssSelector("input[data-qa=\"company\"]");
    private static final By ADDRESS = By.cssSelector("input[data-qa=\"address\"]");
    private static final By ADDRESS2 = By.cssSelector("input[data-qa=\"address2\"]");
    private static final By COUNTRY = By.cssSelector("select[data-qa=\"country\"]");
    private static final By STATE = By.cssSelector("input[data-qa=\"state\"]");
    private static final By CITY = By.cssSelector("input[data-qa=\"city\"]");
    private static final By ZIPCODE = By.cssSelector("input[data-qa=\"zipcode\"]");
    private static final By MOBILE_NUMBER = By.cssSelector("input[data-qa=\"mobile_number\"]");
    private static final By CREATE_ACCOUNT_BTN = By.cssSelector("[data-qa=\"create-account\"]");

    public AccountInformationPage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        visible(CREATE_ACCOUNT_BTN);
    }

    private void selectTitleGender(String title) {
        if ("Mr".equalsIgnoreCase(title)) {
            click(TITLE_GENDER_MR);
        } else if ("Mrs".equalsIgnoreCase(title)) {
            click(TITLE_GENDER_MRS);
        } else {
            throw new IllegalArgumentException("titleGender non correct" + title);
        }
    }

    private void selectWithLocatorAndValue(By locator, String value) {
        WebElement locatorElement = visible(locator);
        Select locatorselect = new Select(locatorElement);
        locatorselect.selectByValue(value);
    }

    public void fillMandatoryFields(SignupData data) {
        assertLoaded();

        WebElement nameInput = visible(NAME);
        WebElement emailInput = visible(EMAIL);

        selectTitleGender(data.getTitle());
        assertThat(nameInput.getAttribute("value")).isEqualTo(data.getName());
        assertThat(emailInput.getAttribute("value")).isEqualTo(data.getEmail());
        clearAndSendKeys(PASSWORD, data.getPassword());
        selectWithLocatorAndValue(DAY, data.getDayOfBirth());
        selectWithLocatorAndValue(MONTH, data.getMonthOfBirth());
        selectWithLocatorAndValue(YEAR, data.getYearOfBirth());
        clearAndSendKeys(FIRST_NAME, data.getFirstName());
        clearAndSendKeys(LAST_NAME, data.getLastName());
        clearAndSendKeys(COMPANY, data.getCompany());
        clearAndSendKeys(ADDRESS, data.getAddress());
        clearAndSendKeys(ADDRESS2, data.getAddress2());
        selectWithLocatorAndValue(COUNTRY, data.getCountry());
        clearAndSendKeys(STATE, data.getState());
        clearAndSendKeys(CITY, data.getCity());
        clearAndSendKeys(ZIPCODE, data.getZipcode());
        clearAndSendKeys(MOBILE_NUMBER, data.getMobileNumber());

    }

    public AccountCreatedPage submitCreateAccount() {
        assertLoaded();
        clickAndWaitUrlContainsFast(CREATE_ACCOUNT_BTN, "/account_created");
        return new AccountCreatedPage(driver);
    }
}
