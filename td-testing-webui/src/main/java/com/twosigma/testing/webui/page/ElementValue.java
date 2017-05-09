package com.twosigma.testing.webui.page;

import com.twosigma.testing.expectation.ActualValueExpectations;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.expectation.equality.EqualMatcher;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.expectation.timer.ExpectationTimerConfigProvider;
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

    public Should getShould() {
        return new Should(this);
    }

    public Wait getWaitTo() {
        return new Wait(this);
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        ElementValueExpectationSteps.shouldStep(this.parent, this, this.describe(), valueMatcher);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ElementValueExpectationSteps.waitStep(this.parent, this, this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    class Should {
        private ElementValue actual;

        Should(ElementValue actual) {
            this.actual = actual;
        }

        public boolean equals(Object expected) {
            ElementValueExpectationSteps.shouldStep(actual.parent, actual, actual.describe(), EqualMatcher.equal(expected));
            return true;
        }
    }

    class Wait {
        private ElementValue actual;

        Wait(ElementValue actual) {
            this.actual = actual;
        }

        public boolean equals(Object expected) {
            ElementValueExpectationSteps.waitStep(actual.parent, actual, actual.describe(), EqualMatcher.equal(expected),
                    ExpectationTimerConfigProvider.createExpectationTimer(),
                    ExpectationTimerConfigProvider.defaultTickMillis(),
                    ExpectationTimerConfigProvider.defaultTimeoutMillis());
            return true;
        }
    }
}
