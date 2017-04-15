package com.twosigma.testing.webui;

import com.twosigma.testing.webui.driver.WebDriverCreator;
import com.twosigma.testing.webui.page.PageElement;

import static com.twosigma.testing.webui.WebTestDsl.$;
import static com.twosigma.testing.webui.WebTestDsl.doc;
import static com.twosigma.testing.webui.WebTestDsl.open;
import static com.twosigma.testing.webui.documentation.DocumentationDsl.badge;

/**
 * @author mykola
 */
public class WebTestDslDemo {
    public static void main(String[] args) {
        open("http://google.com");

        PageElement logo = $("#hplogo");
        doc.withAnnotations(badge(logo)).capture("test");

        WebDriverCreator.closeAll();
    }
}
