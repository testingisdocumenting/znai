package com.twosigma.testing.webtau.page.path.finder;

import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webtau.page.path.ElementsFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.reporter.IntegrationTestsMessageBuilder.*;

/**
 * @author mykola
 */
public class ByCssFinder implements ElementsFinder {
    private String css;

    public ByCssFinder(String css) {
        this.css = css;
    }

    @Override
    public List<WebElement> find(WebDriver driver) {
        return driver.findElements(By.cssSelector(css));
    }

    @Override
    public TokenizedMessage description(boolean isFirst) {
        TokenizedMessage byCssMessage = tokenizedMessage(selectorType("by css"), selectorValue(css));
        return isFirst ? byCssMessage : tokenizedMessage(selectorType("nested find by css"), selectorValue(css));
    }
}
