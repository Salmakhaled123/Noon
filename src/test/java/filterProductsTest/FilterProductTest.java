package filterProductsTest;

import baseTest.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.filterProducts.FilterProductPage;
import pages.filterProducts.ProductInfo;

import java.util.List;

public class FilterProductTest extends BaseTest {

    @BeforeMethod
    public void goToCategory() {
        FilterProductPage filterProductsPage = new FilterProductPage(driver);
        filterProductsPage.navigateToElectronicsCategory(
                configHandler.getValue("electronicsCategoryUrl"));
        filterProductsPage.clickOnSamsungBrand(configHandler.getValue("samsungUrl"));
    }

    @Test
    public void filterProductBasedOnPriceAndRate() {
        FilterProductPage filterProductsPage = new FilterProductPage(driver);
        filterProductsPage.clickOnPriceExpansionTile();
        filterProductsPage.enterMinAndMaxPrice(
                jsonFileManager.getValue("minPrice").toString(),
                jsonFileManager.getValue("maxPrice").toString());
        filterProductsPage.clickOnApplyPriceFilter();
        filterProductsPage.clickOnProductRatingTile();
        filterProductsPage.changeSliderValue(4);
        double targetRate = filterProductsPage.getSliderValue();
        double minPrice = Double.parseDouble(jsonFileManager.getValue("minPrice").toString());
        double maxPrice = Double.parseDouble(jsonFileManager.getValue("maxPrice").toString());
        List<ProductInfo> filteredProducts = filterProductsPage.
                verifyPriceAndRateFilterOnProducts();

        for (ProductInfo productInfo : filteredProducts) {
            softAssert.assertTrue(
                    productInfo.getPrice() >= minPrice && productInfo.getPrice() <= maxPrice,
                    "Price is not in range: " + productInfo.getPrice());
            softAssert.assertTrue(productInfo.getRate() >= targetRate,
                    "Rate is not in range: " + productInfo.getRate());
        }
        softAssert.assertAll();


    }

    @Test
    public void filterProductBasedOnPrice() {
        FilterProductPage filterProductsPage = new FilterProductPage(driver);
        filterProductsPage.clickOnPriceExpansionTile();
        filterProductsPage.enterMinAndMaxPrice(
                jsonFileManager.getValue("minPrice").toString(),
                jsonFileManager.getValue("maxPrice").toString());

        filterProductsPage.clickOnApplyPriceFilter();

        List<Double> prices = filterProductsPage.verifyPriceFilterOnProducts();

        double minPrice = Double.parseDouble(jsonFileManager.getValue("minPrice").toString());

        double maxPrice = Double.parseDouble(jsonFileManager.getValue("maxPrice").toString());

        prices.forEach(price -> softAssert.assertTrue(price >= minPrice
                        && price <= maxPrice,

                "Price is not in range: " + price));

        softAssert.assertAll();
    }

    @Test
    public void filterProductBasedOnRate() {
        FilterProductPage filterProductsPage = new FilterProductPage(driver);
        filterProductsPage.clickOnProductRatingTile();
        filterProductsPage.changeSliderValue(4);
        double targetRate = filterProductsPage.getSliderValue();
        List<Double> rates = filterProductsPage.verifyRateFilterOnProducts();
        rates.forEach(rate -> softAssert.assertTrue(rate >= targetRate,
                "product rate is not in range"));

        softAssert.assertAll();
    }

}
