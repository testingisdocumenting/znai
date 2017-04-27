package com.twosigma.testing.webui.page.path;

import com.twosigma.testing.reporter.TokenizedMessage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class ElementPathEntry {
    private ElementsSelector selector;
    private List<ElementsFilter> filters;

    public ElementPathEntry(ElementsSelector selector) {
        this.selector = selector;
        this.filters = new ArrayList<>();
    }

    public List<WebElement> find(WebDriver driver, WebElement parent) {
        List<WebElement> elements = selector.select(driver);
        if (elements.isEmpty()) {
            return elements;
        }

        List<WebElement> filtered = elements;
        for (ElementsFilter filter : filters) {
            filtered = filter.filter(filtered);
            if (filtered.isEmpty()) {
                return filtered;
            }
        }

        return filtered;
    }

    public TokenizedMessage toTokenizedMessage() {
        return selector.tokenizedDescription();
    }

    @Override
    public String toString() {
        return selector.description();
    }
}
