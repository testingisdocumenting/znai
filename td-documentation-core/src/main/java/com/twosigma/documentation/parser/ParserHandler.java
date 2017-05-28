package com.twosigma.documentation.parser;

import com.twosigma.documentation.extensions.PluginParams;

/**
 * There are multiple markup languages out there. There are common elements among them.
 * This interface attempts to formalize elements and let define common actions to perform regardless of the markup.
 *
 * @author mykola
 */
public interface ParserHandler {
    void onSectionStart(String title);
    void onSectionEnd();
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
    void onSnippet(String lang, String lineNumber, String snippet);
    void onThematicBreak();
    void onIncludePlugin(PluginParams pluginParams);
    void onFencePlugin(PluginParams pluginParams, String content);
    void onInlinedCodePlugin(PluginParams pluginParams);
    void onParsingEnd();
}
