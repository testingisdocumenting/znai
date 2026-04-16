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

package org.testingisdocumenting.znai.parser.commonmark;

import org.commonmark.ext.footnotes.FootnoteReference;
import org.commonmark.node.LinkReferenceDefinition;
import org.commonmark.parser.InlineParserContext;
import org.commonmark.parser.beta.LinkInfo;
import org.commonmark.parser.beta.LinkProcessor;
import org.commonmark.parser.beta.LinkResult;
import org.commonmark.parser.beta.Scanner;

/**
 * Creates FootnoteReference nodes even when a FootnoteDefinition is not present
 * in the same parsed document. This allows footnote references inside
 * container blocks (like attention-note, tabs) to resolve against
 * definitions from the outer page.
 */
public class FootnoteLinkProcessorWithoutDefinitionCheck implements LinkProcessor {
    @Override
    public LinkResult process(LinkInfo linkInfo, Scanner scanner, InlineParserContext context) {
        if (linkInfo.destination() != null) {
            return LinkResult.none();
        }

        String text = linkInfo.text();
        if (!text.startsWith("^")) {
            return LinkResult.none();
        }

        // if it matches a link reference definition, it's not a footnote
        if (linkInfo.label() != null && context.getDefinition(LinkReferenceDefinition.class, linkInfo.label()) != null) {
            return LinkResult.none();
        }

        String label = text.substring(1);
        return LinkResult.replaceWith(new FootnoteReference(label), linkInfo.afterTextBracket());
    }
}
