package com.twosigma.testing.webtau;

import com.twosigma.testing.webtau.page.PageElement;

import static com.twosigma.testing.webtau.WebTauDsl.$;
import static com.twosigma.testing.webtau.WebTauDsl.doc;
import static com.twosigma.testing.webtau.WebTauDsl.open;
import static com.twosigma.testing.webtau.documentation.DocumentationDsl.arrow;
import static com.twosigma.testing.webtau.documentation.DocumentationDsl.badge;
import static com.twosigma.testing.webtau.documentation.DocumentationDsl.highlighter;

/**
 * @author mykola
 */
public class WebTauDslDemo {
    public static void main(String[] args) {
        PageElement signIn = $("#gb_70");
        PageElement input = $(".gsfi");
        PageElement search = $("[name='btnK']");

        open("http://google.com");

        doc.withAnnotations(
                badge(signIn),
                highlighter(input).withColor("green"),
                arrow(search, "Click This").withColor("yellow")).capture("test");
    }
}
