package com.twosigma.testing.webtau.page.path.filter;

import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webtau.page.path.ElementsFilter;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.reporter.IntegrationTestsMessageBuilder.selectorType;
import static com.twosigma.testing.reporter.IntegrationTestsMessageBuilder.selectorValue;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class ByRegexpElementsFilter implements ElementsFilter {
    private Pattern regexp;

    public ByRegexpElementsFilter(Pattern regexp) {
        this.regexp = regexp;
    }

    @Override
    public List<WebElement> filter(List<WebElement> original) {
        return original.stream().filter(el -> regexp.matcher(el.getText()).find()).collect(toList());
    }

    @Override
    public TokenizedMessage description() {
        return tokenizedMessage(selectorType("element(s) with regexp"), selectorValue(regexp.pattern()));
    }
}
