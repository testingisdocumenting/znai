package com.twosigma.testing.examples;

import com.twosigma.webtau.browser.page.PageElement;

import static com.twosigma.webtau.WebTauDsl.$;
import static com.twosigma.webtau.WebTauDsl.browser;
import static com.twosigma.webtau.browser.documentation.DocumentationDsl.arrow;
import static com.twosigma.webtau.browser.documentation.DocumentationDsl.badge;
import static com.twosigma.webtau.browser.documentation.DocumentationDsl.highlighter;

public class WebTauDslDemo {
    public static void main(String[] args) {
        PageElement signIn = $("#gb_70");
        PageElement input = $(".gsfi");
        PageElement search = $("[name='btnK']");

        browser.open("http://google.com");

        browser.doc.withAnnotations(
                badge(signIn),
                highlighter(input).withColor("green"),
                arrow(search, "Click This").withColor("yellow")).capture("test");
    }
}
