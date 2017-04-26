package com.twosigma.testing.webui;

import com.twosigma.testing.http.HttpUrl;
import com.twosigma.testing.webui.cfg.Configuration;
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
    private static Configuration cfg = Configuration.INSTANCE;

    private static final WebDriver driver = new CurrentWebDriver();
    public static final DocumentationDsl doc = new DocumentationDsl(driver);

    public static void open(String url) {
        String fullUrl = createFullUrl(url);
        System.out.println(fullUrl);
        driver.get(fullUrl);
    }

    public static PageElement $(String css) {
        return new GenericPageElement(driver, ElementPath.css(css));
    }

    private static String createFullUrl(String url) {
        if (HttpUrl.isFull(url)) {
            return url;
        }

        return HttpUrl.concat(cfg.getBaseUrl(), url);
    }
}
