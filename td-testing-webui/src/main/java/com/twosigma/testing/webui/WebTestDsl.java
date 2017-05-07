package com.twosigma.testing.webui;

import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.http.HttpUrl;
import com.twosigma.testing.reporter.TestStep;
import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webui.cfg.Configuration;
import com.twosigma.testing.webui.documentation.DocumentationDsl;
import com.twosigma.testing.webui.driver.CurrentWebDriver;
import com.twosigma.testing.webui.expectation.VisibleValueMatcher;
import com.twosigma.testing.webui.page.PageElement;
import com.twosigma.testing.webui.page.path.ElementPath;
import com.twosigma.testing.webui.page.path.GenericPageElement;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.*;

/**
 * @author mykola
 */
public class WebTestDsl {
    private static Configuration cfg = Configuration.INSTANCE;

    private static final WebDriver driver = new CurrentWebDriver();
    public static final DocumentationDsl doc = new DocumentationDsl(driver);

    public static <E> void executeStep(E context,
                                       TokenizedMessage inProgressMessage,
                                       Supplier<TokenizedMessage> completionMessageSupplier,
                                       Runnable action) {
        TestStep<E> step = TestStep.create(context, inProgressMessage, completionMessageSupplier, action);
        step.execute();
    }

    public static void open(String url) {
        String fullUrl = createFullUrl(url);
        executeStep(null, tokenizedMessage(action("opening"), urlValue(fullUrl)),
                () -> tokenizedMessage(action("opened"), urlValue(fullUrl)),
                () -> driver.get(fullUrl));
    }

    public static PageElement $(String css) {
        return new GenericPageElement(driver, ElementPath.css(css));
    }

    public static ValueMatcher beVisible() {
        return new VisibleValueMatcher();
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
