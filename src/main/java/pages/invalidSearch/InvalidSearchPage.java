package pages.invalidSearch;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.BasePage;

public class InvalidSearchPage  extends BasePage {
    private final By searchInputField = By.id("search-input");
    private  final  By errorImage=By.xpath("//img[@alt='An error has occurred']");
    private  final  By errorTitle=By.xpath("//h3[text()='An error has occurred']");

    public InvalidSearchPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getSearchInputField() {
        return findElement(searchInputField);
    }
    public WebElement getErrorImage() {
        return findElement(errorImage);
    }
    public WebElement getErrorTitle() {
        return findElement(errorTitle);
    }
    public void enterInvalidSearchKeyword(String keyword) {
        getSearchInputField().click();
        getSearchInputField().sendKeys(keyword);
        getSearchInputField().sendKeys(Keys.ENTER);
    }
    public  void  openWebsite(String url)
    {
        navigateToUrl(url);
    }




}
