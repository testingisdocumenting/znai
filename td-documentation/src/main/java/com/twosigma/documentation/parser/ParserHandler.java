package com.twosigma.documentation.parser;

/**
 * There are multiple markup languages out there. There are common elements among them.
 * This interface attempts to formalize elements and let define common actions to perform regardless of the markup.
 *
 * @author mykola
 */
public interface ParserHandler {
    void onSectionStart(String title);
    void onSectionEnd();
    void onParagraphStart();
    void onParagraphEnd();
    void onSimpleText(String value);
    void onLink(String label, String anchor);
    void onSnippet(String lang, String lineNumber, String snippet);
    void onInclude(String pluginId, String value); // TODO custom values
}
