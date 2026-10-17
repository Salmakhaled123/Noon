package pages.addToCart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AddToCartPage extends BasePage {
    private static final Logger log = LogManager.getLogger(AddToCartPage.class);
    private final By searchInputField = By.id("search-input");
    private final By addToCartButton = By.xpath(".//button[contains(@class,'atcCta')]");
    private final By productBox = By.xpath("//div[@data-qa='plp-product-box']");
    private final By productShipping = By.xpath(".//*[text()='Free Delivery']");
    private final By productsList = By.xpath("//div[@data-qa='plp-grid']");
    private final By productTitle = By.xpath(".//h2[@data-qa='plp-product-box-name']");
    private final By productPrice = By.xpath(".//div[@data-qa='plp-product-box-price']//strong");
    private final By cartBadge = By.xpath("//a[@data-qa='btn_cartLink-Header-Desktop']");
    private final By cartItemFreeShipping = By.xpath(".//small[text()='Free Shipping']");
    private final By itemsCount = By.xpath("//div[contains(@class,'itemsBadge')]");
    private final By cartTotal = By.xpath("//div[contains(@class,'totalRow')]//div[contains(@class,'amount')]//b");
    private final By checkOutButton = By.xpath("//div[contains(@class,'checkoutCta')]");
    private final By cartItemQuantity = By.xpath(".//input[@data-qa='item-quantity-input']");
    private final By cartItems = By.xpath("//div[contains(@class,'_mainRow_')]");
    private final By cartItemTitle = By.xpath(".//a[contains(@class,'titleLink')]//semibold");
    private final By cartItemPrice = By.xpath(".//div[@data-qa='item-unit-price']");
    private final By loader = By.xpath("//div[contains(@class,'_topLineLoader_')]");
    private  final  By orderSummary=By.xpath("//div[contains(@class, 'itemsBadge')]");
    public AddToCartPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getSearchInputField() {
        return findElement(searchInputField);
    }

    public WebElement getAddToCartButton() {
        return findElement(addToCartButton);
    }

    public List<WebElement> getProductsList() {
        return findElements(productsList, Duration.ofSeconds(20));
    }

    public WebElement getProductPrice() {
        return findElement(productPrice);
    }

    public WebElement getProductTitle() {
        return findElement(productTitle);
    }

    public WebElement getProductShipping() {
        return findElement(productShipping);
    }

    public WebElement getCartItemQuantity() {
        return findElement(cartItemQuantity);
    }

    public List<WebElement> getProductBox() {
        return findElements(productBox, Duration.ofSeconds(20));
    }

    public WebElement getCartBadge() {
        return findElement(cartBadge);
    }

    public List<WebElement> getCartItemsFreeShippingText() {

        return findElements(cartItemFreeShipping, Duration.ofSeconds(20));
    }

    public List<WebElement> getCartItems() {

        return findElements(cartItems, Duration.ofSeconds(30));
    }

    public WebElement getCartItemPrice() {

        return findElement(cartItemPrice);
    }

    public WebElement getCartItemTitle() {

        return findElement(cartItemTitle);
    }
    public  WebElement getOrderSummary() {
        return findElement(orderSummary);
    }


    public WebElement getItemsCount() {
        return findElement(itemsCount);
    }

    public WebElement getCheckOutButton() {
        return findElement(checkOutButton);
    }

    public WebElement getCartTotal() {
        return findElement(cartTotal);
    }


    public void setSearchInputField(String productName) {
        WebElement searchInput = getSearchInputField();
        searchInput.click();
        searchInput.sendKeys(Keys.CONTROL + "a");
        searchInput.sendKeys(Keys.BACK_SPACE);
        searchInput.sendKeys(productName);
        searchInput.sendKeys(Keys.ENTER);
    }

    public void addToCart(List<String> productNames, List<String> productPrices) {
        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i).toLowerCase().trim();
            String targetPrice = productPrices.get(i).trim();
            searchForProduct(productName);
            boolean added = findAndAddMatchingProduct(productName, targetPrice);
            if (!added) {
                log.warn("NOT FOUND: '{}' with price {}", productName, targetPrice);
            }
        }
    }

    private void searchForProduct(String productName) {
        List<WebElement> oldResults = getProductBox();
        setSearchInputField(productName);
        if (!oldResults.isEmpty()) {
            try {
                wait.until(ExpectedConditions.stalenessOf(oldResults.get(0)));
            } catch (TimeoutException e) {
                log.warn("results did not refresh for '{}'", productName);
            }
        }
    }

    private boolean findAndAddMatchingProduct(String productName, String targetPrice) {
        wait.ignoring(StaleElementReferenceException.class);

        return wait.until(d -> {
            List<WebElement> products = d.findElements(productBox);
            if (products.isEmpty()) {
                return null;
            }

            for (WebElement product : products) {
                if (isMatchingProduct(product, productName, targetPrice)) {
                    product.findElement(addToCartButton).click();
                    log.info("SUCCESS: Added '{}' with price {} to cart", productName, targetPrice);
                    return true;
                }
            }
            return false;
        });
    }

    private boolean isMatchingProduct(WebElement product, String productName, String targetPrice) {
        List<WebElement> prices = product.findElements(productPrice);
        List<WebElement> titles = product.findElements(productTitle);
        if (prices.isEmpty() || titles.isEmpty()) {
            return false;
        }

        String price = prices.get(0).getText().replace(",", "").trim();
        String title = titles.get(0).getText().toLowerCase().trim();
        boolean freeShipping = !product.findElements(productShipping).isEmpty();

        return price.equals(targetPrice) && freeShipping
                && titleMatches(title, productName);
    }

    public boolean titleMatches(String title, String productName) {
        for (String word : productName.split("\\s+")) {
            if (!title.contains(word)) {
                return false;
            }
        }
        return true;
    }

    public List<String> validateTheQuantityOfEachProductInCart() {
        List<String> quantities = new ArrayList<>();
        for (WebElement cartItem : getCartItems()) {
            quantities.add(cartItem.findElement(cartItemQuantity).
                    getAttribute("value"));
        }
        return quantities;

    }

    public List<Boolean> validateTheShippingOfEachProductInCart() {
        List<Boolean> freeShipping = new ArrayList<>();
        for (WebElement cartItem : getCartItems()) {
            freeShipping.add(cartItem.findElement(cartItemFreeShipping).isDisplayed());
        }
        return freeShipping;

    }
    public  int validateTheCountOfProductsInsideCart()
    {
        String cartCount=getOrderSummary().getText().split(" ")[0];
        return Integer.parseInt(cartCount);
    }

    public void navigateToCart() {
        getCartBadge().click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
    }
    public  List<Boolean> validateProductPriceInsideCart(List<String[]>csvRows)
    {
        List<WebElement>productsCart=getCartItems();
        List<Boolean>foundProducts=new ArrayList<>();
        for (String[] csvRow : csvRows) {
            String csvProductPrice = csvRow[1];
            boolean isFound = false;
            for (WebElement product : productsCart) {
                String productPriceCart = product.findElement(cartItemPrice).getText()
                        .replace(",", "").
                        replace("EGP", "");
                if (Double.parseDouble(productPriceCart) == Double.parseDouble(csvProductPrice)
                       ) {
                    isFound = true;
                    break;

                }
            }
            foundProducts.add(isFound);
        }
        return  foundProducts;

    }


}
