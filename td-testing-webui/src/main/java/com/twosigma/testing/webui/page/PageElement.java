package com.twosigma.testing.webui.page;

import com.twosigma.testing.expectation.ActualValue;
import com.twosigma.testing.expectation.ActualValueExpectations;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.equality.EqualMatcher;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.reporter.TestStep;
import com.twosigma.testing.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.*;

/**
 * @author mykola
 */
public interface PageElement extends ActualValueExpectations {
    PageElement all();
    WebElement findElement();
    ElementValue elementValue();
    void setValue(Object value);
    void sendKeys(String keys);
    void clear();
    boolean isVisible();
    TokenizedMessage describe();

    @Override
    default void should(ValueMatcher valueMatcher) {
        PageElementExpectationSteps.shouldStep(this, valueMatcher);
    }

    default Should getShould() {
        return new Should(this);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher) {
        PageElementExpectationSteps.waitStep(this, valueMatcher);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, long timeOutMillis) {
        ActualValue.actual(this).waitTo(valueMatcher, timeOutMillis);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        ActualValue.actual(this).waitTo(valueMatcher, tickMillis, timeOutMillis);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ActualValue.actual(this).waitTo(valueMatcher, expectationTimer, tickMillis, timeOutMillis);
    }

    class Should {
        private PageElement actual;

        Should(PageElement actual) {
            this.actual = actual;
        }

        public boolean equals(Object expected) {
            PageElementExpectationSteps.shouldStep(actual, EqualMatcher.equal(expected));
            return true;
        }
    }
}
