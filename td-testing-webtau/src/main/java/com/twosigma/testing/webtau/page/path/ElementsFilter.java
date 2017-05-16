package com.twosigma.testing.webtau.page.path;

import com.twosigma.testing.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author mykola
 */
public interface ElementsFilter {
    List<WebElement> filter(List<WebElement> original);
    TokenizedMessage description();
}
