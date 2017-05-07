package com.twosigma.testing.webui.page;

import com.twosigma.testing.expectation.ActualValue;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.reporter.TestStep;
import com.twosigma.testing.reporter.TokenizedMessage;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.*;

/**
 * @author mykola
 */
class PageElementExpectationSteps {
    private PageElementExpectationSteps() {
    }

    public static void shouldStep(PageElement pageElement, ValueMatcher valueMatcher) {
        expectationStep(pageElement, valueMatcher,
                tokenizedMessage(action("expecting")),
                () -> ActualValue.actual(pageElement).should(valueMatcher));
    }

    public static void waitStep(PageElement pageElement, ValueMatcher valueMatcher) {
        expectationStep(pageElement, valueMatcher,
                tokenizedMessage(action("waiting"), TO),
                () -> ActualValue.actual(pageElement).waitTo(valueMatcher));
    }

    private static void expectationStep(PageElement pageElement, ValueMatcher valueMatcher, TokenizedMessage messageStart, Runnable expectationValidation) {
        TestStep<PageElement> step = TestStep.create(pageElement,
                messageStart.add(pageElement.describe()).add(matcher(valueMatcher.matchingMessage())),
                () -> tokenizedMessage(pageElement.describe()).add(matcher(valueMatcher.matchedMessage(null, pageElement))),
                expectationValidation);

        step.execute();
    }
}
