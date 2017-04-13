package com.twosigma.testing.webui.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class WebDriverCreator {
    private static List<WebDriver> drivers = new ArrayList<>();

    public static WebDriver create() {
        System.setProperty("webdriver.chrome.driver", "/Users/mykola/work/chromedriver");
        return register(new ChromeDriver());
    }

    public static void closeAll() {
        drivers.forEach(WebDriver::close);
        drivers.clear();
    }

    private static WebDriver register(WebDriver driver) {
        drivers.add(driver);
        return driver;
    }
}
