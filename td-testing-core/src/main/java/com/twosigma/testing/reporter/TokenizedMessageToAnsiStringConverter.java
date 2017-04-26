package com.twosigma.testing.reporter;

import com.twosigma.console.ansi.AutoResetAnsiString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TokenizedMessageToAnsiStringConverter {
    private Map<String, List<Object>> ansiSequencesByToken;

    public TokenizedMessageToAnsiStringConverter() {
        ansiSequencesByToken = new HashMap<>();
    }

    public void associate(String tokenType, Object... ansiSequence) {
        ansiSequencesByToken.put(tokenType, Arrays.asList(ansiSequence));
    }

    public AutoResetAnsiString convert(TokenizedMessage tokenizedMessage) {
        return new AutoResetAnsiString(
                tokenizedMessage.tokensStream().flatMap(this::convertToAnsiSequence));
    }

    private Stream<?> convertToAnsiSequence(MessageToken messageToken) {
        List<Object> ansiSequence = ansiSequencesByToken.get(messageToken.getType());
        return Stream.concat(ansiSequence.stream(), Stream.of(messageToken.getValue()));
    }
}
