/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.testing.examples;

import com.twosigma.webtau.browser.page.PageElement;

import static com.twosigma.webtau.WebTauDsl.$;
import static com.twosigma.webtau.WebTauDsl.browser;
import static com.twosigma.webtau.browser.documentation.BrowserDocumentation.*;

public class WebTauDslDemo {
    public static void main(String[] args) {
        PageElement signIn = $("#gb_70");
        PageElement input = $(".gsfi");
        PageElement search = $("[name='btnK']");

        browser.open("http://google.com");

        browser.doc.withAnnotations(
                badge(signIn),
                highlight(input).withColor("green"),
                arrow(search, "Click This").withColor("yellow")).capture("test");
    }
}
