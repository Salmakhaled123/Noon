package addToCart;

import baseTest.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.addToCart.AddToCartPage;

import java.util.List;

public class AddToCartTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(AddToCartTest.class);

    @BeforeMethod
    public void addToCartTest() {
        AddToCartPage addToCartPage = new AddToCartPage(driver);
        addToCartPage.addToCart(
                csvFileManager.
                        GetSpecificColumnData("productName"),
                csvFileManager.
                        GetSpecificColumnData("price"));

    }

    @Test
    public void goToCartPageTestAndValidate() {
        AddToCartPage addToCartPage = new AddToCartPage(driver);
        addToCartPage.navigateToCart();
        List<String> quantities = addToCartPage.validateTheQuantityOfEachProductInCart();
        for (String quantity : quantities) {
            softAssert.assertEquals(quantity, "1");
        }
        List<Boolean> freeShipping = addToCartPage.validateTheShippingOfEachProductInCart();
        for (Boolean isFreeShipping : freeShipping) {
            softAssert.assertTrue(isFreeShipping, "Free shipping is not available");
        }
        double actualTotal = Double.parseDouble(addToCartPage.getCartTotal().getText().
                replace(",", "").replace("EGP", "")
                .trim());
        softAssert.assertEquals(actualTotal, 10000.00
        );
        int count = addToCartPage.validateTheCountOfProductsInsideCart();
        softAssert.assertEquals(count, csvFileManager.getRows().size());
        log.info("Number of products added to cart: {}", count);
        List<Boolean> foundProducts = addToCartPage.validateProductPriceInsideCart
                (csvFileManager.getRows());
        for (Boolean found : foundProducts) {
            softAssert.assertTrue(found, " price does not match");
        }
        softAssert.assertAll();

    }


}
