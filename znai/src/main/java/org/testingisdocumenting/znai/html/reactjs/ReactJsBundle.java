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

import org.testingisdocumenting.znai.html.Deployer;
import org.testingisdocumenting.znai.web.WebResource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ReactJsBundle {
    private final WebResource mainJs;
    private final WebResource mainCss;
    private final WebResource bootstrapCss;
    private final WebResource katexCss;

    private final List<WebResource> fonts;

    public ReactJsBundle() {
        mainJs = WebResource.fromResource("static/main.js");
        mainCss = WebResource.fromResource("static/main.css");
        bootstrapCss = WebResource.fromResource("static/css/bootstrap.min.css");
        katexCss = WebResource.fromResource("static/css/katex.min.css");

        Stream<WebResource> glyphIcons = Stream.of("eot", "svg", "ttf", "woff", "woff2")
                .map(ext -> "static/fonts/glyphicons-halflings-regular." + ext)
                .map(WebResource::fromResource);

        Stream<WebResource> katexFonts = Stream.of("ttf", "woff", "woff2")
                .flatMap(ext -> Stream.of(
                        "KaTeX_Main-Bold." + ext,
                        "KaTeX_Main-BoldItalic." + ext,
                        "KaTeX_Main-Italic." + ext,
                        "KaTeX_Main-Regular." + ext,
                        "KaTeX_Math-BoldItalic." + ext,
                        "KaTeX_Math-Italic." + ext)
                        .map(name -> WebResource.fromResource("static/fonts/" + name)));

        fonts = Stream.concat(glyphIcons, katexFonts).collect(toList());
    }

    public Stream<WebResource> clientJavaScripts() {
        return Stream.of(mainJs);
    }

    public Stream<WebResource> clientCssResources() {
        return Stream.of(bootstrapCss, katexCss, mainCss);
    }

    public void deploy(Deployer deployer) {
        Stream.concat(Stream.concat(clientJavaScripts(), clientCssResources()),
                fonts.stream()).forEach(deployer::deploy);
    }
}
