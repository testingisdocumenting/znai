package com.twosigma.testing.webui.page;

import org.openqa.selenium.WebElement;

/**
 * @author mykola
 */
public interface PageElement {
    PageElement all();
    WebElement findElement();
    ElementValue elementValue();
}
