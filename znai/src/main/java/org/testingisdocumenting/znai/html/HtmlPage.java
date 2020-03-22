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

package org.testingisdocumenting.znai.html;

import org.testingisdocumenting.znai.web.WebResource;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class HtmlPage {
    public static final String FAVICON_PATH = "favicon.png";

    private final String customFavIconPath;

    private String title;

    private List<WebResource> cssResources;
    private List<WebResource> javaScriptResources;

    private List<RenderSupplier> headSuppliers;
    private List<RenderSupplier> bodySuppliers;
    private List<RenderSupplier> javaScriptSuppliers;

    public HtmlPage() {
        this("");
    }

    public HtmlPage(String customFavIconPath) {
        this.customFavIconPath = customFavIconPath;

        title = "";

        cssResources = new ArrayList<>();
        javaScriptResources = new ArrayList<>();

        headSuppliers = new ArrayList<>();
        bodySuppliers = new ArrayList<>();
        javaScriptSuppliers = new ArrayList<>();
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void addCss(WebResource webResource) {
        cssResources.add(webResource);
    }

    public void addJavaScript(WebResource webResource) {
        javaScriptResources.add(webResource);
    }

    public void addJavaScriptInFront(WebResource webResource) {
        javaScriptResources.add(0, webResource);
    }

    public void addToHead(RenderSupplier supplier) {
        headSuppliers.add(supplier);
    }

    public void addToBody(RenderSupplier supplier) {
        bodySuppliers.add(supplier);
    }

    public void addToJavaScript(RenderSupplier supplier) {
        javaScriptSuppliers.add(supplier);
    }

    public String render(String documentationId) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\" /> \n" +
                "<title>" + title + "</title>" +
                headSuppliers.stream().map(RenderSupplier::render).collect(joining("\n")) +
                cssResources.stream().map(r -> r.generateCssLink(documentationId)).collect(joining("\n")) +
                "\n</head>\n" +
                "<link rel=\"shortcut icon\" href=" + favIconPath(documentationId) + "type=\"image/ico\"/>\n" +
                "<body>\n" +
                bodySuppliers.stream().map(RenderSupplier::render).collect(joining("\n")) + "\n" +
                javaScriptResources.stream().map(r -> r.generateJavaScriptLink(documentationId)).collect(joining("\n")) + "\n" +
                "<script>\n" +
                javaScriptSuppliers.stream().map(RenderSupplier::render).collect(joining("\n")) + "\n" +
                "</script>\n" +
                "\n</body>" +
                "\n</html>\n";
    }

    private String favIconPath(String documentationId) {
        if (!customFavIconPath.isEmpty()) {
            return makeIconPath(customFavIconPath);
        }

        return documentationId.isEmpty() ?
                makeIconPath("/" + FAVICON_PATH):
                makeIconPath("/" + documentationId + "/" + FAVICON_PATH);
    }

    private String makeIconPath(String path) {
        return "\"" + path + "\"";
    }
}
