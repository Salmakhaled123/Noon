package driverFactory;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverFactory {
    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    public static WebDriver getWebDriver(String browserName) {
        WebDriver driver;
        switch (browserName.toLowerCase().trim()) {
            case "chrome":
                driver = GetChromeDriver.getChromeDriver();
                break;
            case "edge":
                driver = GetEdgeDriver.getEdgeDriver();
                break;
            case "firefox":
                driver = GetFireFoxDriver.getFireFoxDriver();
                break;
            default:
                log.warn("Invalid browser name in get WebDriver: {}",browserName);
                throw new IllegalArgumentException("Invalid browser name : " + browserName);

        }
        return driver;
    }

    public static void quitWebDriver(String browserName)
    {
        switch (browserName.toLowerCase().trim()) {
            case "chrome":
                GetChromeDriver.quitDriver();
                break;
            case "edge":
                GetEdgeDriver.quitDriver();
                break;
            case "firefox":
                GetFireFoxDriver.quitDriver();
                break;
            default:
                log.warn("Invalid browser name in quit web driver: {}",browserName);
                throw new IllegalArgumentException("Invalid browser name : " + browserName);

        }
    }
}


