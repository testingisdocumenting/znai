/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.parser;

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;

import java.util.Map;

/**
 * There are multiple markup languages out there. There are common elements among them.
 * This interface attempts to formalize elements and let define common actions to perform regardless of the markup.
 *
 */
public interface ParserHandler {
    /**
     * top level section start handler. Should be responsible for handle of closing current section (if required)
     * @param title title of a new section
     * @param headingProps heading props to change look and feel
     */
    void onSectionStart(String title, HeadingProps headingProps);
    void onSectionEnd();

    void onSubHeading(int level, String title, HeadingProps headingProps);
    void onHardLineBreak();
    void onSoftLineBreak();
    void onParagraphStart();
    void onParagraphEnd();
    void onBulletListStart(char bulletMarker, boolean tight);
    void onBulletListEnd();
    void onOrderedListStart(char delimiter, int startNumber);
    void onOrderedListEnd();
    void onListItemStart();
    void onListItemEnd();
    void onTable(MarkupTableData tableData);
    void onEmphasisStart();
    void onEmphasisEnd();
    void onStrongEmphasisStart();
    void onStrongEmphasisEnd();
    void onStrikeThroughStart();
    void onStrikeThroughEnd();
    void onBlockQuoteStart();
    void onBlockQuoteEnd();
    void onSimpleText(String value);
    void onInlinedCode(String inlinedCode, DocReferences docReferences);
    void onLinkStart(String url);
    void onLinkEnd();
    void onImage(String title, String destination, String alt);
    void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet);
    void onThematicBreak();

    void onCustomNodeStart(String nodeName, Map<String, ?> attrs);
    void onCustomNode(String nodeName, Map<String, ?> attrs);
    void onCustomNodeEnd(String nodeName);

    void onDocElement(DocElement docElement);

    void onGlobalAnchor(String id);
    void onGlobalAnchorRefStart(String id);
    void onGlobalAnchorRefEnd();

    /**
     * @param includePlugin already process plugin
     * @param pluginResult result of plugin process
     */
    void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult);
    void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult);
    void onInlinedCodePlugin(InlinedCodePlugin inlinedCodePlugin, PluginResult pluginResult);
    void onParsingEnd();
}
