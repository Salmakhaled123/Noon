package driverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class GetEdgeDriver {
    private static Logger log = LogManager.getLogger(GetEdgeDriver.class);
    private  static  WebDriver driver=null;
    public  static WebDriver getEdgeDriver()
    {
        if(driver==null)
        {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--incognito");
            driver=new EdgeDriver(edgeOptions);
            log.info("driver isn't null now :");

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
