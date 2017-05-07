package com.twosigma.testing.reporter;

import com.twosigma.utils.TraceUtils;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author mykola
 */
public class TestStep<E> {
    private E context;

    private TokenizedMessage inProgressMessage;
    private Supplier<TokenizedMessage> completionMessageSupplier;
    private Runnable action;
    private TokenizedMessage completionMessage;

    private boolean isInProgress;
    private boolean isSuccessful;

    private List<TestStep> children;
    private String stackTrace;

    public TestStep(E context,
                    TokenizedMessage inProgressMessage,
                    Supplier<TokenizedMessage> completionMessageSupplier,
                    Runnable action) {
        this.context = context;
        this.inProgressMessage = inProgressMessage;
        this.completionMessageSupplier = completionMessageSupplier;
        this.action = action;
        this.isInProgress = true;
    }

    public void execute() {
        try {
            StepReporters.onStart(this);
            action.run();

            complete(completionMessageSupplier.get());
            StepReporters.onSuccess(this);
        } catch (Throwable e) {
            fail(e);
            StepReporters.onFailure(this);
            throw e;
        }

    }

    public TokenizedMessage getInProgressMessage() {
        return inProgressMessage;
    }

    public TokenizedMessage getCompletionMessage() {
        return completionMessage;
    }

    private void complete(TokenizedMessage message) {
        isInProgress = false;
        isSuccessful = true;
        completionMessage = message;
    }

    private void fail(Throwable t) {
        stackTrace = TraceUtils.stackTrace(t);
        completionMessage = new TokenizedMessage();
        completionMessage.add("error", "failed").add(inProgressMessage).add("delimiter", ":")
                .add("error", t.getMessage());
    }
}
