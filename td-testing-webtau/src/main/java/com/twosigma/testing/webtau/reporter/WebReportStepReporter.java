package com.twosigma.testing.webtau.reporter;

import com.twosigma.testing.reporter.StepReporter;
import com.twosigma.testing.reporter.TestStep;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class WebReportStepReporter implements StepReporter {
    private List<TestStep> firstLevelSteps = new ArrayList<>();

    public List<TestStep> getStepsAndReset() {
        List<TestStep> result = firstLevelSteps;
        firstLevelSteps = new ArrayList<>();

        return result;
    }

    @Override
    public void onStepStart(TestStep step) {
        if (step.getNumberOfParents() == 0) {
            firstLevelSteps.add(step);
        }
    }

    @Override
    public void onStepSuccess(TestStep step) {

    }

    @Override
    public void onStepFailure(TestStep step) {

    }
}
