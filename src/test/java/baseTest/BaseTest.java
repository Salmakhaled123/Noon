package baseTest;

import driverFactory.DriverFactory;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import utilities.CSVFileManager;
import utilities.ConfigHandler;
import utilities.JSONFileManager;
import utilities.ScreenShot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class BaseTest {
    private Logger log = LogManager.getLogger(BaseTest.class);

    public static   WebDriver driver;
    public SoftAssert softAssert;
    public WebDriverWait wait;
    public ConfigHandler configHandler;
    public JSONFileManager jsonFileManager,jsonInvalidSearch;
    public CSVFileManager csvFileManager;
    @BeforeMethod
    public void setUp() {
        jsonFileManager=new JSONFileManager("src/main/resources/minAndMaxPrice.json");
        jsonInvalidSearch=new JSONFileManager("src/main/resources/invalidSearchKeywords.json");
        configHandler=new ConfigHandler("src/main/resources/config.properties");
        csvFileManager=new CSVFileManager("src/main/resources/noonProducts.csv");
        driver = DriverFactory.getWebDriver(configHandler.getValue("browserName"));
        driver.get(configHandler.getValue("websiteUrl"));
        driver.manage().window().maximize();
        softAssert=new SoftAssert();
        wait=new WebDriverWait(driver, Duration.ofSeconds(10));
        log.info("setup driver Successfully");


    }
    @AfterMethod
    public  void failedTestCases(ITestResult result) throws IOException {
        if(result.getStatus()==ITestResult.FAILURE)
        {
            File image= ScreenShot.takeScreenShot(driver);
            FileInputStream fis=new FileInputStream(image);
            Allure.addAttachment("Failure screenshot for Tc : "
                            +result.getMethod().getMethodName(),"image/png",
                    fis,"png");
        }
    }
    @AfterMethod
    public void tearDown()
    {
        DriverFactory.quitWebDriver(configHandler.getValue("browserName"));
        driver=null;
        log.info(" driver is null now in teardown");

    }
}
