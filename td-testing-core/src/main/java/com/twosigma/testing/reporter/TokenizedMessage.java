package com.twosigma.testing.reporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TokenizedMessage implements Iterable<MessageToken> {
    private List<MessageToken> tokens;

    public TokenizedMessage() {
        tokens = new ArrayList<>();
    }

    public static TokenizedMessage build(MessageToken... tokens) {
        TokenizedMessage message = new TokenizedMessage();
        message.add(tokens);

        return message;
    }

    public TokenizedMessage add(String type, Object value) {
        return add(new MessageToken(type, value));
    }

    public TokenizedMessage add(MessageToken... tokens) {
        this.tokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public TokenizedMessage add(TokenizedMessage tokenizedMessage) {
        tokenizedMessage.tokensStream().forEach(this::add);
        return this;
    }

    public int getNumberOfTokens() {
        return tokens.size();
    }

    public Stream<MessageToken> tokensStream() {
        return tokens.stream();
    }

    @Override
    public Iterator<MessageToken> iterator() {
        return tokens.iterator();
    }
}
