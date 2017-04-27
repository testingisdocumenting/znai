package com.twosigma.testing.webui.reporter;

import com.twosigma.console.ansi.Color;
import com.twosigma.testing.reporter.MessageToken;
import com.twosigma.testing.reporter.TokenizedMessageToAnsiConverter;

import java.util.Arrays;

/**
 * @author mykola
 */
public class WebUiMessageBuilder {
    private enum TokenTypes {
        ACTION("action", true, Color.BLUE),
        STRING_VALUE("stringValue", true, Color.GREEN),
        SELECTOR_TYPE("selectorType", true, Color.YELLOW),
        SELECTOR_VALUE("selectorValue", true, Color.GREEN),
        PREPOSITION("preposition", true, Color.BLACK);

        private final String type;
        private final boolean delimiterAfter;
        private final Object[] styles;

        TokenTypes(String type, boolean delimiterAfter, Object... styles) {
            this.type = type;
            this.delimiterAfter = delimiterAfter;
            this.styles = styles;
        }

        public MessageToken token(Object value) {
            return new MessageToken(type, value);
        }
    }

    public static final MessageToken TO = TokenTypes.PREPOSITION.token("to");

    private static final TokenizedMessageToAnsiConverter converter = createConverter();

    public static MessageToken stringValue(Object value) {
        return TokenTypes.STRING_VALUE.token(value.toString());
    }

    public static MessageToken action(String action) {
        return TokenTypes.ACTION.token(action);
    }

    public static MessageToken selectorType(String selector) {
        return TokenTypes.SELECTOR_TYPE.token(selector);
    }

    public static MessageToken selectorValue(String selector) {
        return TokenTypes.SELECTOR_VALUE.token(selector);
    }

    public static TokenizedMessageToAnsiConverter getConverter() {
        return converter;
    }

    private static TokenizedMessageToAnsiConverter createConverter() {
        TokenizedMessageToAnsiConverter c = new TokenizedMessageToAnsiConverter();

        Arrays.stream(TokenTypes.values()).forEach(t -> {
            c.associate(t.type, t.delimiterAfter, t.styles);
        });

        return c;
    }
}
