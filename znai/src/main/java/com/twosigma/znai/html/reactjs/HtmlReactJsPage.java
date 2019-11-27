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

package com.twosigma.znai.html.reactjs;

import com.twosigma.znai.html.HtmlPage;
import com.twosigma.znai.html.RenderSupplier;
import com.twosigma.znai.utils.JsonUtils;
import com.twosigma.znai.web.WebResource;
import com.twosigma.znai.web.extensions.WebSiteResourcesProviders;

import java.util.Map;

public class HtmlReactJsPage {
    private static final String REACT_BLOCK_ID = "znai";

    private final ReactJsBundle reactJsBundle;

    public HtmlReactJsPage(ReactJsBundle reactJsBundle) {
        this.reactJsBundle = reactJsBundle;
    }

    public HtmlPage create(String title,
                           String component,
                           Map<String, ?> props,
                           RenderSupplier mainBodySupplier,
                           String favIconCustomPath) {
        HtmlPage htmlPage = new HtmlPage(favIconCustomPath);
        htmlPage.setTitle(title);

        RenderSupplier createElementStatement = () -> "React.createElement(" + component + ", " +
                JsonUtils.serializePrettyPrint(props) + ")";

        htmlPage.addToBody(() -> "<div id=\"" + REACT_BLOCK_ID + "\">" +
                mainBodySupplier.render() + "</div>");

        htmlPage.addToJavaScript(() -> "document.getElementById('" + REACT_BLOCK_ID + "').innerHTML = '';\n" +
                "ReactDOM.render(" + createElementStatement.render() + ", " +
                "document.getElementById(\"" + REACT_BLOCK_ID + "\"));");

        reactJsBundle.clientJavaScripts().forEach(htmlPage::addJavaScript);
        reactJsBundle.clientCssResources().forEach(htmlPage::addCss);

        WebSiteResourcesProviders.jsResources().forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.jsClientOnlyResources().forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.cssResources().forEach(htmlPage::addCss);
        WebSiteResourcesProviders.htmlHeadResources().map(WebResource::getTextContent)
                .forEach(text -> htmlPage.addToHead(() -> text));
        WebSiteResourcesProviders.htmlBodyResources().map(WebResource::getTextContent)
                .forEach(text -> htmlPage.addToBody(() -> text));

        return htmlPage;
    }
}
