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

package org.testingisdocumenting.znai.website;

import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.ResourceUtils;

import java.nio.file.Path;

public class WebResource {
    private final Path originPath;
    private final String path;
    private final byte[] resourceContent;
    private final boolean isModule;

    private WebResource(final Path originPath, final String path, boolean isModule) {
        this.originPath = originPath;
        this.path = path;
        this.resourceContent = null;
        this.isModule = isModule;
    }

    private WebResource(final Path originPath, final String path) {
        this(originPath, path, false);
    }

    private WebResource(final String resourcePath, boolean isModule) {
        this.originPath = null;
        this.path = resourcePath;
        this.resourceContent = ResourceUtils.binaryContent(resourcePath);
        this.isModule = isModule;
    }

    private WebResource(final String resourcePath) {
        this(resourcePath, false);
    }

    private WebResource(final String path, final byte[] content) {
        this.originPath = null;
        this.path = path;
        this.resourceContent = content;
        this.isModule = false;
    }

    public static WebResource withPath(final String path) {
        return new WebResource(null, path);
    }

    public static WebResource moduleWithPath(final String path) {
        return new WebResource(null, path, true);
    }

    public static WebResource withPath(final Path originPath, final String webPath) {
        return new WebResource(originPath, webPath);
    }

    public static WebResource fromResource(final String resourcePath) {
        return new WebResource(resourcePath);
    }

    public static WebResource moduleFromResource(final String resourcePath) {
        return new WebResource(resourcePath, true);
    }

    public static WebResource withTextContent(final String path, final String content) {
        return new WebResource(path, content.getBytes());
    }

    public Path getOriginPath() {
        return originPath;
    }

    public String getPath() {
        return path;
    }

    public String generateCssLink(String documentationId) {
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + pathForHtml(documentationId) + "\">";
    }

    public String generateJavaScriptLink(String documentationId) {
        var type = isModule ? "module" : "text/javascript";
        return "<script type=\"" + type + "\" src=\"" + pathForHtml(documentationId) + "\"></script>";
    }

    private String pathForHtml(String documentationId) {
        return "/" + ((documentationId.isEmpty()) ? "" : documentationId + "/") + path;
    }

    public byte[] getBinaryContent() {
        return resourceContent == null ?
                FileUtils.fileBinaryContent(originPath):
                resourceContent;
    }

    public String getTextContent() {
        return new String(getBinaryContent());
    }

    @Override
    public String toString() {
        return (resourceContent == null ? "FromDisk" : "FromResource") + "{" +
                "originPath=" + originPath +
                ", path='" + path + '\'' +
                '}';
    }
}
