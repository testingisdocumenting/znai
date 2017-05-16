package com.twosigma.testing.webtau.page;

import com.twosigma.testing.expectation.ActualValue;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.reporter.TestStep;
import com.twosigma.testing.reporter.TokenizedMessage;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.webtau.reporter.WebUiMessageBuilder.*;

/**
 * @author mykola
 */
class ElementValueExpectationSteps {
    private ElementValueExpectationSteps() {
    }

    public static void shouldStep(PageElement owner, ElementValue<?> value, TokenizedMessage valueDescription, ValueMatcher valueMatcher) {
        expectationStep(owner, value, valueDescription, valueMatcher, false,
                tokenizedMessage(action("expecting")),
                () -> ActualValue.actual(value).should(valueMatcher));
    }

    public static void shouldNotStep(PageElement owner, ElementValue<?> value, TokenizedMessage valueDescription, ValueMatcher valueMatcher) {
        expectationStep(owner, value, valueDescription, valueMatcher, true,
                tokenizedMessage(action("expecting")),
                () -> ActualValue.actual(value).shouldNot(valueMatcher));
    }

    public static void waitStep(PageElement owner, ElementValue<?> value, TokenizedMessage valueDescription, ValueMatcher valueMatcher,
                                ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        expectationStep(owner, value, valueDescription, valueMatcher, false,
                tokenizedMessage(action("waiting"), TO),
                () -> ActualValue.actual(value).waitTo(valueMatcher, expectationTimer, tickMillis, timeOutMillis));
    }

    public static void waitNotStep(PageElement owner, ElementValue<?> value, TokenizedMessage valueDescription, ValueMatcher valueMatcher,
                                ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        expectationStep(owner, value, valueDescription, valueMatcher, true,
                tokenizedMessage(action("waiting"), TO),
                () -> ActualValue.actual(value).waitToNot(valueMatcher, expectationTimer, tickMillis, timeOutMillis));
    }

    private static void expectationStep(PageElement owner, ElementValue<?> value, TokenizedMessage elementDescription,
                                        ValueMatcher valueMatcher, boolean isNegative,
                                        TokenizedMessage messageStart, Runnable expectationValidation) {
        TestStep<PageElement> step = TestStep.create(owner,
                messageStart.add(elementDescription)
                        .add(matcher(isNegative ? valueMatcher.negativeMatchingMessage() : valueMatcher.matchingMessage())),
                () -> tokenizedMessage(elementDescription)
                        .add(matcher(isNegative ?
                                valueMatcher.negativeMatchedMessage(null, value):
                                valueMatcher.matchedMessage(null, value))),
                expectationValidation);

        step.execute();
    }
}
