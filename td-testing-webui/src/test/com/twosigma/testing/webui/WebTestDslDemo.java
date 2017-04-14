package com.twosigma.testing.webui;

import com.twosigma.testing.webui.driver.WebDriverCreator;

import static com.twosigma.testing.webui.WebTestDsl.doc;
import static com.twosigma.testing.webui.WebTestDsl.open;

/**
 * @author mykola
 */
public class WebTestDslDemo {
    public static void main(String[] args) {
        open("http://google.com");
        doc.capture("test.png");
        WebDriverCreator.closeAll();
    }
}
