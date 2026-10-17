package driverFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


public class GetFireFoxDriver {
    private static  Logger log = LogManager.getLogger(GetFireFoxDriver.class);
    private  static  WebDriver driver=null;
    public  static WebDriver getFireFoxDriver()
    {
        if(driver==null)
        {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--private");
            driver= new FirefoxDriver(firefoxOptions);
            log.info("driver isn't null now: ");
        }
        return  driver;

    }
    public static  void quitDriver()
    {
        if(driver!=null)
        {
            driver.quit();
            driver=null;
            log.info("quit the driver successfully");

        }
    }
}
