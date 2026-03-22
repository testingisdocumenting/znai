/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.diagrams.mermaid;

import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MermaidLinkResolver {
    // matches: click nodeId "url" or click nodeId href "url"
    private static final Pattern CLICK_URL_PATTERN = Pattern.compile(
            "click\\s+\\S+\\s+(?:href\\s+)?\"([^\"]+)\"");

    private MermaidLinkResolver() {
    }

    static String validateAndResolveLinks(DocStructure docStructure, Path markupPath, String mermaidContent) {
        Matcher matcher = CLICK_URL_PATTERN.matcher(mermaidContent);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String url = matcher.group(1);
            if (UrlUtils.isExternal(url)) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group()));
                continue;
            }

            DocUrl docUrl = new DocUrl(url);
            docStructure.validateUrl(markupPath, "inside mermaid diagram", docUrl);

            String resolvedUrl = docStructure.createUrl(markupPath, docUrl);
            String replacement = matcher.group().replace("\"" + url + "\"", "\"" + resolvedUrl + "\"");
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
