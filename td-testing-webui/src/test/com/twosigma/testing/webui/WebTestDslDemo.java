package com.twosigma.testing.webui;

import com.twosigma.testing.webui.documentation.DocumentationDsl;
import com.twosigma.testing.webui.driver.WebDriverCreator;
import com.twosigma.testing.webui.page.PageElement;

import static com.twosigma.testing.webui.WebTestDsl.$;
import static com.twosigma.testing.webui.WebTestDsl.doc;
import static com.twosigma.testing.webui.WebTestDsl.open;
import static com.twosigma.testing.webui.documentation.DocumentationDsl.arrow;
import static com.twosigma.testing.webui.documentation.DocumentationDsl.badge;
import static com.twosigma.testing.webui.documentation.DocumentationDsl.highlighter;

/**
 * @author mykola
 */
public class WebTestDslDemo {
    public static void main(String[] args) {
        PageElement signIn = $("#gb_70");
        PageElement input = $(".gsfi");
        PageElement search = $("[name='btnK']");

        open("http://google.com");

        doc.withAnnotations(badge(signIn),
                highlighter(input).withColor("green"),
                arrow(search, "Click This").withColor("yellow")).capture("test");
    }
}
