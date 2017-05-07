package com.twosigma.testing.reporter;

import com.twosigma.utils.TraceUtils;

import java.util.ArrayList;
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

    private List<TestStep<?>> children;
    private TestStep<?> parent;
    private String stackTrace;

    private static ThreadLocal<TestStep<?>> currentStep = new ThreadLocal<>();

    public static <E> TestStep<E> create(E context,
                                         TokenizedMessage inProgressMessage,
                                         Supplier<TokenizedMessage> completionMessageSupplier,
                                         Runnable action) {
        TestStep<E> step = new TestStep<>(context, inProgressMessage, completionMessageSupplier, action);
        TestStep<?> localCurrentStep = TestStep.currentStep.get();

        step.parent = localCurrentStep;
        if (localCurrentStep != null) {
            localCurrentStep.children.add(localCurrentStep);
        }
        currentStep.set(step);

        return step;
    }

    private TestStep(E context,
                     TokenizedMessage inProgressMessage,
                     Supplier<TokenizedMessage> completionMessageSupplier,
                     Runnable action) {
        this.context = context;
        this.children = new ArrayList<>();
        this.inProgressMessage = inProgressMessage;
        this.completionMessageSupplier = completionMessageSupplier;
        this.action = action;
        this.isInProgress = true;
    }

    public int getNumberOfParents() {
        int result = 0;
        TestStep step = this;
        while (step.parent != null) {
            result++;
            step = step.parent;
        }

        return result;
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
        } finally {
            TestStep<?> localCurrentStep = TestStep.currentStep.get();
            if (localCurrentStep != null) {
                currentStep.set(localCurrentStep.parent);
            }
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
