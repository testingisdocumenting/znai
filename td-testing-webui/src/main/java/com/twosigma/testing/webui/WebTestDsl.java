package com.twosigma.testing.webui;

import com.twosigma.testing.webui.documentation.DocumentationDsl;
import com.twosigma.testing.webui.driver.CurrentWebDriver;
import com.twosigma.testing.webui.page.PageElement;
import com.twosigma.testing.webui.page.path.ElementPath;
import com.twosigma.testing.webui.page.path.GenericPageElement;
import com.twosigma.testing.webui.page.path.selector.CssSelector;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * @author mykola
 */
public class WebTestDsl {
    private static final WebDriver driver = new CurrentWebDriver();
    public static final DocumentationDsl doc = new DocumentationDsl((TakesScreenshot) driver);

    public static void open(String url) {
        driver.get(url);
    }

    public static PageElement $(String css) {
        ElementPath path = new ElementPath();
        path.addSelector(new CssSelector(css));

        return new GenericPageElement(driver, path);
    }
}
