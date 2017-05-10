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

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        ElementValueExpectationSteps.shouldNotStep(this, this.elementValue(), this.describe(), valueMatcher);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ElementValueExpectationSteps.waitStep(this, this.elementValue(), this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    default void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ElementValueExpectationSteps.waitNotStep(this, this.elementValue(), this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    default ShouldAndWaitProperty getShould() {
        return new ShouldAndWaitProperty<>(this, PageElement::should);
    }

    default ShouldAndWaitProperty getShouldNot() {
        return new ShouldAndWaitProperty<>(this, PageElement::shouldNot);
    }

    default ShouldAndWaitProperty getWaitTo() {
        return new ShouldAndWaitProperty<>(this, PageElement::waitTo);
    }

    default ShouldAndWaitProperty getWaitNotTo() {
        return new ShouldAndWaitProperty<>(this, PageElement::waitToNot);
    }
}
