package com.twosigma.testing.webui.page;

import com.twosigma.testing.expectation.ActualValue;
import com.twosigma.testing.expectation.ActualValueExpectations;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.equality.EqualMatcher;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;

/**
 * @author mykola
 */
public interface PageElement extends ActualValueExpectations {
    PageElement all();
    ElementValue<Integer> getCount();
    WebElement findElement();
    ElementValue elementValue();
    void setValue(Object value);
    void sendKeys(String keys);
    void clear();
    boolean isVisible();
    TokenizedMessage describe();

    @Override
    default void should(ValueMatcher valueMatcher) {
        ElementValueExpectationSteps.shouldStep(this, this.elementValue(), this.describe(), valueMatcher);
    }

    default Should getShould() {
        return new Should(this);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ElementValueExpectationSteps.waitStep(this, this.elementValue(), this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    class Should {
        private PageElement actual;

        Should(PageElement actual) {
            this.actual = actual;
        }

        public boolean equals(Object expected) {
            ElementValueExpectationSteps.shouldStep(actual, actual.elementValue(), actual.describe(), EqualMatcher.equal(expected));
            return true;
        }
    }
}
