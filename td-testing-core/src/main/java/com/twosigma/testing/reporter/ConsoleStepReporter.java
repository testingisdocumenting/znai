package com.twosigma.testing.reporter;

import com.twosigma.console.ConsoleOutputs;

/**
 * @author mykola
 */
public class ConsoleStepReporter implements StepReporter {
    private TokenizedMessageToAnsiConverter toAnsiConverter;

    public ConsoleStepReporter(TokenizedMessageToAnsiConverter toAnsiConverter) {
        this.toAnsiConverter = toAnsiConverter;
    }

    @Override
    public void onStart(TestStep step) {
        ConsoleOutputs.out(toAnsiConverter.convert(step.getInProgressMessage()));
    }

    @Override
    public void onSuccess(TestStep step) {
        ConsoleOutputs.out(toAnsiConverter.convert(step.getCompletionMessage()));
    }

    @Override
    public void onFailure(TestStep step) {
        ConsoleOutputs.out(toAnsiConverter.convert(step.getCompletionMessage()));
    }
}
