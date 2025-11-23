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

package org.testingisdocumenting.znai.html.reactjs;

import org.testingisdocumenting.znai.html.HtmlPage;
import org.testingisdocumenting.znai.html.RenderSupplier;
import org.testingisdocumenting.znai.utils.JsonUtils;
import org.testingisdocumenting.znai.website.WebResource;
import org.testingisdocumenting.znai.website.WebSiteResourcesProviders;
import org.testingisdocumenting.znai.utils.ResourceUtils;

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
                // code snippets sometimes have <script> tags, even though they are in quotes,
                // because it is embedded into html page <script> block, browser parser get confused
                // but if it is surrounded by this special comment, it tricks browser to be "normal" again
                "/*<!--*/\n" +
                ResourceUtils.textContent("template/initialization.js") + "\n" +
                "window.ReactDOM.render(" + createElementStatement.render() + ", " +
                "document.getElementById(\"" + REACT_BLOCK_ID + "\"));\n" +
                "/*-->*/\n");

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
