package com.twosigma.documentation.parser;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.table.MarkupTableData;

/**
 * There are multiple markup languages out there. There are common elements among them.
 * This interface attempts to formalize elements and let define common actions to perform regardless of the markup.
 *
 * @author mykola
 */
public interface ParserHandler {
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
    void onBlockQuoteStart();
    void onBlockQuoteEnd();
    void onSimpleText(String value);
    void onInlinedCode(String inlinedCode);
    void onLinkStart(String url);
    void onLinkEnd();
    void onImage(String title, String destination, String alt);
    void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet);
    void onThematicBreak();

    void onGlobalAnchor(String id);
    void onGlobalAnchorRef(String id, String label);

    /**
     * @param includePlugin already process plugin
     * @param pluginResult result of plugin process
     */
    void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult);
    void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult);
    void onInlinedCodePlugin(PluginParams pluginParams);
    void onParsingEnd();
}
