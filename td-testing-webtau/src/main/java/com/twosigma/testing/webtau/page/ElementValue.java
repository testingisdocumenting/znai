package com.twosigma.testing.webtau.page;

import com.twosigma.testing.expectation.ActualValueExpectations;
import com.twosigma.testing.expectation.ShouldAndWaitProperty;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.reporter.StepReportOptions;
import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.reporter.IntegrationTestsMessageBuilder;
import com.twosigma.testing.reporter.ValueMatcherExpectationSteps;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.reporter.IntegrationTestsMessageBuilder.OF;

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
        this.description = tokenizedMessage(IntegrationTestsMessageBuilder.classifier(name)).add(OF).add(parent.describe());
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
        ValueMatcherExpectationSteps.shouldStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitNotStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    public ShouldAndWaitProperty getShould() {
        return new ShouldAndWaitProperty<>(this, this::should);
    }

    public ShouldAndWaitProperty getShouldNot() {
        return new ShouldAndWaitProperty<>(this, this::shouldNot);
    }

    public ShouldAndWaitProperty getWaitTo() {
        return new ShouldAndWaitProperty<>(this, this::waitTo);
    }

    public ShouldAndWaitProperty getWaitToNot() {
        return new ShouldAndWaitProperty<>(this, this::waitToNot);
    }
}
