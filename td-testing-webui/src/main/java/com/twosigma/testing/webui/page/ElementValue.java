package com.twosigma.testing.webui.page;

import com.twosigma.testing.expectation.ActualValueExpectations;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webui.reporter.WebUiMessageBuilder;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.OF;

/**
 * @author mykola
 */
public class ElementValue<E> implements ActualValueExpectations {
    private PageElement parent;
    private String name;
    private ElementValueFetcher<E> valueFetcher;

    private TokenizedMessage description;

    public ElementValue(PageElement parent, String name, ElementValueFetcher<E> valueFetcher) {
        this.parent = parent;
        this.name = name;
        this.valueFetcher = valueFetcher;
        this.description = tokenizedMessage(WebUiMessageBuilder.classifier(name)).add(OF).add(parent.describe());
    }

    public PageElement getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public E get() {
        return valueFetcher.fetch();
    }

    public TokenizedMessage describe() {
        return this.description;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        ElementValueExpectationSteps.shouldStep(this.parent, this, this.describe(), valueMatcher);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        ElementValueExpectationSteps.shouldNotStep(this.parent, this, this.describe(), valueMatcher);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ElementValueExpectationSteps.waitStep(this.parent, this, this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ElementValueExpectationSteps.waitNotStep(this.parent, this, this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    public ShouldAndWaitProperty getShould() {
        return new ShouldAndWaitProperty<>(this, ElementValue::should);
    }

    public ShouldAndWaitProperty getShouldNot() {
        return new ShouldAndWaitProperty<>(this, ElementValue::shouldNot);
    }

    public ShouldAndWaitProperty getWaitTo() {
        return new ShouldAndWaitProperty<>(this, ElementValue::waitTo);
    }

    public ShouldAndWaitProperty getWaitNotTo() {
        return new ShouldAndWaitProperty<>(this, ElementValue::waitToNot);
    }
}
