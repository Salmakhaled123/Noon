package pages.filterProducts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FilterProductPage extends BasePage {
    private static final Logger log = LogManager.getLogger(FilterProductPage.class);
    private final By samsungBrand = By.xpath
            ("//a[contains(@href,'/egypt-en/samsung/')]");
    private final By priceExpansionTile = By.xpath("//div[@data-qa='Price']//h3[text()='Price']");
    private final By brandExpansionTile = By.xpath("//div[@data-qa='Brand']//h3[text()='Brand']");

    private final By minInputPriceField = By.xpath("//input[@data-qa='filter-min-input']");
    private final By maxInputPriceField = By.xpath("//input[@data-qa='filter-max-input']");
    private final By applyPriceFilter = By.xpath("//button[@type='submit']");
    private final By productBoxInsideProductsList =
            By.xpath("//div[@data-qa='plp-product-box']");
    private final By productRatingSection = By.xpath("//div[@data-qa='Product Rating']//h3[text()='Product Rating']");
    private final By sliderLocator =
            By.xpath("//input[contains(@class,'slider') and @type='range']");
    private final By priceAmount = By.xpath(".//strong[contains(@class, '_amount')]");
    private final By productRate = By.xpath(".//div[contains(@class,'_textCtr')]");

    public FilterProductPage(WebDriver driver) {
        super(driver);
    }



    public WebElement getPriceExpansionTile() {

        return findElement(priceExpansionTile);
    }

    public WebElement getMinInputPriceField() {

        return findElement(minInputPriceField);
    }

    public WebElement getMaxInputPriceField() {

        return findElement(maxInputPriceField);
    }

    public WebElement getApplyPriceFilter() {

        return findElement(applyPriceFilter);
    }

    public List<WebElement> getProductBoxInsideProductsList() {

        return findElements(productBoxInsideProductsList, Duration.ofSeconds(30));
    }

    public WebElement getProductRating() {

        return findElement(productRatingSection);
    }

    public WebElement getBrandExpansionTile() {

        return findElement(brandExpansionTile);
    }

    public WebElement getSliderLocator() {
        return findElement(sliderLocator);
    }

    public double getSliderValue() {
        return Double.parseDouble(getSliderLocator().getAttribute("value"));
    }


    public void navigateToElectronicsCategory(String url) {
        navigateToUrl(url);
    }


    public List<ProductInfo> verifyPriceAndRateFilterOnProducts() {
        wait.ignoring(StaleElementReferenceException.class);
        List<ProductInfo> result = wait.until(_ -> {
            List<WebElement> products = getProductBoxInsideProductsList();
            if (products.isEmpty()) {
                return null;
            }

            List<ProductInfo> infos = new ArrayList<>();
            for (WebElement product : products) {
                List<WebElement> priceElements = product.findElements(priceAmount);
                List<WebElement> rateElements = product.findElements(productRate);
                if (priceElements.isEmpty() || rateElements.isEmpty()) {
                    continue;
                }
                String price = priceElements.get(0).getText()
                        .replace(",", "").trim();
                String rate = rateElements.get(0).getText().trim();
                if (price.isEmpty() || rate.isEmpty()) {
                    continue;
                }
                infos.add(new ProductInfo(Double.parseDouble(price),
                        Double.parseDouble(rate)));


            }
            for (ProductInfo info : infos) {
                log.info("price: {} | rate: {}", info.getPrice(), info.getRate());
            }
            return infos;
        });

        log.info("products info size: {}", result.size());
        return result;
    }

    public List<Double> verifyPriceFilterOnProducts() {
        List<Double> prices = new ArrayList<>();
        List<WebElement> products =
                wait.until(ExpectedConditions.visibilityOfAllElements(
                        getProductBoxInsideProductsList()));

        for (WebElement product : products) {
            String actualPrice = product.findElement(priceAmount).getText();
            double price = Double.parseDouble(actualPrice.replace(",",
                    "").trim());
            prices.add(price);
        }

        return prices;

    }

    public List<Double> verifyRateFilterOnProducts() {
        wait.ignoring(StaleElementReferenceException.class);
        List<Double> rates = wait.until(_ -> {
            List<WebElement> products = getProductBoxInsideProductsList();
            if (products.isEmpty()) {
                return null;
            }
            List<Double> result = new ArrayList<>();
            for (WebElement product : products) {
                List<WebElement> rateElements = product.findElements(productRate);
                if (rateElements.isEmpty()) {
                    continue;
                }
                String actualRate = rateElements.get(0).getText().trim();
                if (actualRate.isEmpty()) {
                    continue;
                }
                result.add(Double.parseDouble(actualRate));
            }
            return result;
        });
        log.info("rates :{}", rates);
        return rates;
    }

    public void enterMinAndMaxPrice(String minPrice, String maxPrice) {
        Actions actions = new Actions(driver);
        actions.scrollToElement(getMinInputPriceField()).perform();
        getMinInputPriceField().click();
        getMinInputPriceField().clear();
        getMinInputPriceField().sendKeys(minPrice);
        getMaxInputPriceField().click();
        getMaxInputPriceField().clear();
        getMaxInputPriceField().sendKeys(maxPrice);
    }

    public void changeSliderValue(double targetValue) {

        WebElement slider = wait.until(
                ExpectedConditions.presenceOfElementLocated(sliderLocator));
        ((JavascriptExecutor) driver).executeScript(
                "Object.getOwnPropertyDescriptor(HTMLInputElement.prototype,'value')" +
                        ".set.call(arguments[0], arguments[1]);" +
                        "['input','change'].forEach(e => arguments[0].dispatchEvent" +
                        "(new Event(e,{bubbles:true})));",
                slider, String.valueOf(targetValue));
        log.info("slider value changed to :{}", slider.getAttribute("value"));

    }


    public void clickOnSamsungBrand(String url) {

        driver.get(url);

    }

    public void clickOnPriceExpansionTile() {
        getBrandExpansionTile().click();
        getPriceExpansionTile().click();
    }

    public void clickOnApplyPriceFilter() {
        WebElement oldFirstProduct = findElement(productBoxInsideProductsList);

        getApplyPriceFilter().click();
        wait
                .until(ExpectedConditions.stalenessOf(oldFirstProduct));
    }

    public void clickOnProductRatingTile() {
       getBrandExpansionTile().click();
        getProductRating().click();
    }


}
