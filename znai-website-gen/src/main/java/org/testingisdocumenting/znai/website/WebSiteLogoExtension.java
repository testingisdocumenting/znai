/*
 * Copyright 2023 znai maintainers
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.stream.Stream;

public class WebSiteLogoExtension implements WebSiteResourcesProvider {
    private final WebResource cssLogoResource;

    WebSiteLogoExtension(Path docRootPath) {
        Path logoPath = docRootPath.resolve("logo.png");
        this.cssLogoResource = Files.exists(logoPath) ?
                WebResource.withTextContent("logo.css", createLogoCss(logoPath)):
                null;
    }

    @Override
    public Stream<WebResource> cssResources() {
        return cssLogoResource != null ? Stream.of(cssLogoResource) : Stream.empty();
    }

    private String createLogoCss(Path logoPath) {
        return ".znai-documentation-logo {\n" +
                "    display: block;\n" +
                "    background-size: contain;\n" +
                "    background-image: url(data:image/png;base64," + imageToBase64(logoPath) + ")\n" +
                "}\n";
    }

    private String imageToBase64(Path logoPath) {
        byte[] content = FileUtils.fileBinaryContent(logoPath);
        return Base64.getEncoder().encodeToString(content);
    }
}
