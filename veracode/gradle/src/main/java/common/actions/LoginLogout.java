package common.actions;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Then;

public class LoginLogout {

    private static final Logger LOG = LoggerFactory.getLogger(LoginLogout.class);

    private WebDriver driver;

    public LoginLogout(SharedDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    @Then("^login to webapp as user$")
    public void customerAdminLoginSteps() throws Throwable {
        String username = Constants.CUSTOMER_USERNAME;
        customerLogin(Constants.CUSTOMER_USERNAME, Constants.CUSTOMER_PASSWORD);
    }

    /**
     * Launches login page with given URL and verifies page title if specified
     *
     * @param loginUrl
     * @param expectedPageTitle
     * @throws Throwable
     */
    public void launchLoginPage(String loginUrl, String expectedPageTitle) throws Throwable {
        UIActions.loginPage(driver, loginUrl);
        if (expectedPageTitle != null) {
            UIActions.verifyPageTitle(driver, expectedPageTitle);
        }
        UIActions.waitForPageLoaded(driver, (int) Constants.UI_IMPLICIT_WAIT);

    }

    public void customerLogin(String username, String password) throws Throwable {
        UIActions.enterText(driver, username, "email");
        LOG.info("Logging in to Hydra UI as {}", username);
        UIActions.enterText(driver, password, "password");
        LOG.info("Entered password");

        UIActions.clickElement(driver, WebElementMappingConstants.uiToTest.get("Login"));
        LOG.info("Selected login button");
    }

}
