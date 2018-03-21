package com.twosigma.documentation.parser;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.util.Map;

/**
 * There are multiple markup languages out there. There are common elements among them.
 * This interface attempts to formalize elements and let define common actions to perform regardless of the markup.
 *
 * @author mykola
 */
public interface ParserHandler {
    /**
     * top level section start handler. Should be responsible for handle of closing current section (if required)
     * @param title title of a new section
     */
    void onSectionStart(String title);
    void onSectionEnd();

    void onSubHeadingStart(int level);
    void onSubHeadingEnd(int level);
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
    void onInlinedCode(String inlinedCode);
    void onLinkStart(String url);
    void onLinkEnd();
    void onImage(String title, String destination, String alt);
    void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet);
    void onThematicBreak();

    void onCustomNodeStart(String nodeName, Map<String, ?> attrs);
    void onCustomNode(String nodeName, Map<String, ?> attrs);
    void onCustomNodeEnd(String nodeName);

    void onGlobalAnchor(String id);
    void onGlobalAnchorRefStart(String id);
    void onGlobalAnchorRefEnd();

    /**
     * @param includePlugin already process plugin
     * @param pluginResult result of plugin process
     */
    void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult);
    void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult);
    void onInlinedCodePlugin(PluginParams pluginParams);
    void onParsingEnd();
}
