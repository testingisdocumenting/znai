package com.twosigma.testing.webtau.reporter;

import com.twosigma.testing.reporter.StepReporter;
import com.twosigma.testing.reporter.TestStep;
import com.twosigma.testing.webtau.WebTauDsl;
import com.twosigma.testing.webtau.page.PageElement;

/**
 * @author mykola
 */
public class ScreenshotStepReporter implements StepReporter<PageElement> {
    @Override
    public void onStepStart(TestStep<PageElement> step) {
    }

    @Override
    public void onStepSuccess(TestStep<PageElement> step) {
    }

    @Override
    public void onStepFailure(TestStep<PageElement> step) {
        if (step.hasPayload(ScreenshotStepPayload.class)) {
            return;
        }

        step.addPayload(new ScreenshotStepPayload(WebTauDsl.takeScreenshotAsBase64()));
    }
}
