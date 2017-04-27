package com.twosigma.testing.reporter;

import com.twosigma.utils.TraceUtils;

import java.util.List;

/**
 * @author mykola
 */
public class TestStep<E> {
    private E context;

    private TokenizedMessage inProgressMessage;
    private TokenizedMessage completionMessage;

    private boolean isInProgress;
    private boolean isSuccessful;

    private List<TestStep> children;
    private String stackTrace;

    public TestStep(E context, TokenizedMessage inProgressMessage) {
        this.context = context;
        this.inProgressMessage = inProgressMessage;
        this.isInProgress = true;
    }

    public TokenizedMessage getInProgressMessage() {
        return inProgressMessage;
    }

    public TokenizedMessage getCompletionMessage() {
        return completionMessage;
    }

    public void complete(TokenizedMessage message) {
        isInProgress = false;
        isSuccessful = true;
        completionMessage = message;
    }

    public void fail(Throwable t) {
        stackTrace = TraceUtils.stackTrace(t);
        completionMessage = new TokenizedMessage();
        completionMessage.add("error", t.getMessage()).add("", " ").add(inProgressMessage);
    }
}
