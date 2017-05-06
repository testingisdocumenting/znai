package com.twosigma.testing.webui.page;

import com.twosigma.testing.expectation.ActualValue;
import com.twosigma.testing.expectation.ActualValueExpectations;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import org.openqa.selenium.WebElement;

/**
 * @author mykola
 */
public interface PageElement extends ActualValueExpectations {
    PageElement all();
    WebElement findElement();
    ElementValue elementValue();
    void setValue(Object value);
    boolean isVisible();

    @Override
    default void should(ValueMatcher valueMatcher) {
        ActualValue.actual(this).should(valueMatcher);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        ActualValue.actual(this).shouldNot(valueMatcher);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher) {
        ActualValue.actual(this).waitTo(valueMatcher);
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
}
