package com.twosigma.testing.reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TokenizedMessage {
    private List<MessageToken> tokens;

    public TokenizedMessage() {
        tokens = new ArrayList<>();
    }

    public TokenizedMessage add(String type, Object value) {
        tokens.add(new MessageToken(type, value));
        return this;
    }

    public Stream<MessageToken> tokensStream() {
        return tokens.stream();
    }
}
