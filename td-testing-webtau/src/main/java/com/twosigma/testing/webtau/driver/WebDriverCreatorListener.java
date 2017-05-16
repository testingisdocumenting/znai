package com.twosigma.testing.webtau.driver;

import org.openqa.selenium.WebDriver;

/**
 * @author mykola
 */
public interface WebDriverCreatorListener {
    void beforeDriverCreation();
    void afterDriverCreation(WebDriver webDriver);
}
