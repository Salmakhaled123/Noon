package invalidSearchTest;

import baseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.invalidSearch.InvalidSearchPage;

public class InvalidSearchTest extends BaseTest {
    @Test
    public void testInvalidSearch() {
        InvalidSearchPage invalidSearchPage = new InvalidSearchPage(driver);
        invalidSearchPage.openWebsite(configHandler.getValue("websiteUrl"));
        invalidSearchPage.enterInvalidSearchKeyword(
                (String) jsonInvalidSearch.getValue("invalidKeyword")
        );
        Assert.assertTrue(invalidSearchPage.getErrorImage().isDisplayed());
        Assert.assertEquals(invalidSearchPage.getErrorTitle().getText(),
                "An error has occurred");


    }
}
