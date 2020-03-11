package common.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedDriver extends EventFiringWebDriver {

    private static final Logger LOG = LoggerFactory.getLogger(SharedDriver.class);

    public static WebDriver driver;

    public static final String WEBDRIVER_DOWNLOAD_PATH = Constants.DRIVER_LOC;

    public SharedDriver() throws Throwable {
            super(getBrowserDriver());
    }

    public static WebDriver getBrowserDriver() throws Throwable {
            System.setProperty("webdriver.gecko.driver", WEBDRIVER_DOWNLOAD_PATH);
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
            return driver;
    }

}
