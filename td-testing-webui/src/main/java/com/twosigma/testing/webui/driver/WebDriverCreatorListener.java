package com.twosigma.testing.webui.driver;

import org.openqa.selenium.WebDriver;

/**
 * @author mykola
 */
public interface WebDriverCreatorListener {
    void beforeDriverCreation();
    void afterDriverCreation(WebDriver webDriver);
}
