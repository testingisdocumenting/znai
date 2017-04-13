package com.twosigma.testing.webui.page.path;

import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author mykola
 */
public interface ElementsFilter {
    List<WebElement> filter(List<WebElement> original);
}
