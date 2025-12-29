/*
 * Copyright 2025 znai maintainers
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

import org.testingisdocumenting.znai.html.Deployer;
import org.testingisdocumenting.znai.utils.ResourceUtils;
import org.testingisdocumenting.znai.website.WebResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ReactJsBundle {
    public static final ReactJsBundle INSTANCE =  new ReactJsBundle();
    private final WebResource mainCss;
    private final WebResource katexCss;
    private final String javaScripts;

    private final List<WebResource> fonts;

    private ReactJsBundle() {
        mainCss = WebResource.fromResource("static/main.css");
        katexCss = WebResource.fromResource("static/css/katex.min.css");

        javaScripts = ResourceUtils.textContents("META-INF/znai/javascript-files.txt").stream().collect(Collectors.joining("\n"));

        Stream<WebResource> katexFonts = KatexFonts.LIST.stream()
                .map(name -> WebResource.fromResource("static/css/fonts/" + name));

        fonts = katexFonts.collect(toList());
    }

    public Stream<WebResource> clientJavaScripts() {
        return javaScripts.lines().map(WebResource::moduleFromResource);
    }

    public Stream<WebResource> clientCssResources() {
        return Stream.of(katexCss, mainCss);
    }

    public void deploy(Deployer deployer) {
        Stream.concat(Stream.concat(clientJavaScripts(), clientCssResources()),
                fonts.stream()).forEach(deployer::deploy);
    }
}
