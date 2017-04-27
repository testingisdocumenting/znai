package com.twosigma.testing.webui.page.path.selector;

import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webui.page.path.ElementsSelector;
import com.twosigma.testing.webui.reporter.WebUiMessageBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.*;

/**
 * @author mykola
 */
public class CssSelector implements ElementsSelector {
    private String css;

    public CssSelector(String css) {
        this.css = css;
    }

    @Override
    public List<WebElement> select(WebDriver driver) {
        return driver.findElements(By.cssSelector(css));
    }

    @Override
    public String description() {
        return "by css " + css;
    }

    @Override
    public TokenizedMessage tokenizedDescription() {
        return TokenizedMessage.build(selectorType("by css"), selectorValue(css));
    }
}
