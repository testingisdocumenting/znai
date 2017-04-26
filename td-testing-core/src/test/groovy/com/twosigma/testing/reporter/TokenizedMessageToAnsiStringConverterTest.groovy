package com.twosigma.testing.reporter

import com.twosigma.console.ansi.Color
import com.twosigma.console.ansi.FontStyle
import org.junit.Test

/**
 * @author mykola
 */
class TokenizedMessageToAnsiStringConverterTest {
    @Test
    void "should convert to ansi string based on registered tokens"() {
        def converter = new TokenizedMessageToAnsiStringConverter()
        converter.associate("keyword", FontStyle.BOLD, Color.CYAN)
        converter.associate("id", Color.BLUE)
        converter.associate("id2", FontStyle.BOLD, Color.BLUE)

        def message = new TokenizedMessage()
        message.add("keyword", "hello").add("id", "world").add("id2", "world")

        def ansiString = converter.convert(message)
        assert ansiString.toString() == "\u001B[1m\u001B[36mhello\u001B[34mworld\u001B[1m\u001B[34mworld\u001B[0m"
    }
}
