package com.twosigma.testing.webui

import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.utils.RegexpUtils

import java.util.regex.Pattern

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.none

/**
 * @author mykola
 */
class WebTestGroovyDsl extends WebTestDsl {
    private static final Pattern PLACEHOLDER_PATTERN = ~/<(\w+)>/

    private static StandaloneTestRunner testRunner

    static void initWithTestRunner(StandaloneTestRunner testRunner) {
        this.testRunner = testRunner
    }

    static void scenario(String description, Closure code) {
        testRunner.scenario(description, code)
    }

    static Closure action(String description, Closure code) {
        return { args ->
            String withReplacedValues = replacePlaceholders(description, args)

            executeStep(null, tokenizedMessage(none(withReplacedValues)),
                    { -> tokenizedMessage(none("done " + withReplacedValues)) },
                    { -> code.curry(args).call() })
        }
    }

    private static String replacePlaceholders(String description, args) {
        return RegexpUtils.replaceAll(description, PLACEHOLDER_PATTERN, { matcher ->
            return args[matcher.group(1)]
        })
    }
}
