package driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetChromeDriver {
    private static final Logger log = LoggerFactory.getLogger(GetChromeDriver.class);
    private  static  WebDriver driver=null;
    public  static WebDriver getChromeDriver()
    {
        if(driver==null)
        {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--incognito");
            driver=new ChromeDriver(chromeOptions);
            log.info("driver isn't null now");
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
