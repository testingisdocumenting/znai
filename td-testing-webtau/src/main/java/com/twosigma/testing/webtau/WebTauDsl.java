package com.twosigma.testing.webtau;

import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.ranges.GreaterThanMatcher;
import com.twosigma.testing.http.Http;
import com.twosigma.testing.http.HttpUrl;
import com.twosigma.testing.reporter.TestStep;
import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webtau.cfg.WebTauConfig;
import com.twosigma.testing.webtau.documentation.DocumentationDsl;
import com.twosigma.testing.webtau.driver.CurrentWebDriver;
import com.twosigma.testing.webtau.expectation.VisibleValueMatcher;
import com.twosigma.testing.webtau.page.Cookies;
import com.twosigma.testing.webtau.page.PageElement;
import com.twosigma.testing.webtau.page.path.ElementPath;
import com.twosigma.testing.webtau.page.path.GenericPageElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.reporter.IntegrationTestsMessageBuilder.*;

/**
 * @author mykola
 */
public class WebTauDsl {
    public static final WebTauConfig cfg = WebTauConfig.INSTANCE;

    public static final WebDriver driver = new CurrentWebDriver();
    public static final Http http = Http.http;
    public static final DocumentationDsl doc = new DocumentationDsl(driver);
    public static final Cookies cookies = new Cookies(driver);

    public static <E> void executeStep(E context,
                                       TokenizedMessage inProgressMessage,
                                       Supplier<TokenizedMessage> completionMessageSupplier,
                                       Runnable action) {
        TestStep<E> step = TestStep.create(context, inProgressMessage, completionMessageSupplier, action);
        step.execute();
    }

    public static WebTauConfig getCfg() {
        return cfg;
    }

    public static void open(String url) {
        String fullUrl = createFullUrl(url);

        String currentUrl = driver.getCurrentUrl();
        boolean sameUrl = fullUrl.equals(currentUrl);

        executeStep(null, tokenizedMessage(action("opening"), urlValue(fullUrl)),
                () -> tokenizedMessage(action(sameUrl ? "staying at" : "opened"), urlValue(fullUrl)),
                () -> { if (! sameUrl) driver.get(fullUrl); });
    }

    public static void reopen(String url) {
        String fullUrl = createFullUrl(url);

        executeStep(null, tokenizedMessage(action("re-opening"), urlValue(fullUrl)),
                () -> tokenizedMessage(action("opened"), urlValue(fullUrl)),
                () -> driver.get(fullUrl));
    }

    public static String takeScreenshotAsBase64() {
        TakesScreenshot takesScreenshot = (TakesScreenshot) WebTauDsl.driver;
        return takesScreenshot.getScreenshotAs(OutputType.BASE64);
    }

    public static PageElement $(String css) {
        return new GenericPageElement(driver, ElementPath.css(css));
    }

    public static ValueMatcher beVisible() {
        return new VisibleValueMatcher();
    }

    public static ValueMatcher beGreaterThan(Comparable base) {
        return new GreaterThanMatcher(base);
    }

    public static ValueMatcher getBeVisible() {
        return beVisible();
    }

    private static String createFullUrl(String url) {
        if (HttpUrl.isFull(url)) {
            return url;
        }

        return HttpUrl.concat(cfg.getBaseUrl(), url);
    }
}
