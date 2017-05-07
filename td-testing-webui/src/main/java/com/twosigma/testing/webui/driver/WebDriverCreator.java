package com.twosigma.testing.webui.driver;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class WebDriverCreator {
    private static List<WebDriver> drivers = new ArrayList<>();

    static {
        registerCleanup();
    }

    public static WebDriver create() {
        ChromeDriver driver = createChromeDriver();
        initState(driver);

        return register(driver);
    }

    private static ChromeDriver createChromeDriver() {
        return new ChromeDriver();
    }

    public static void closeAll() {
        drivers.forEach(WebDriver::close);
        drivers.clear();
    }

    private static WebDriver register(WebDriver driver) {
        drivers.add(driver);
        return driver;
    }

    private static void initState(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(1000, 800));
    }

    private static void registerCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread(WebDriverCreator::closeAll));
    }
}
