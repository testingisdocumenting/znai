package com.twosigma.testing.expectation.equality;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.utils.StringUtils;

/**
 * @author mykola
 */
public class Mismatch {
    private ActualPath actualPath;
    private String message;
    private String fullMessage;

    public Mismatch(ActualPath actualPath, String message) {
        this.actualPath = actualPath;
        this.message = message;
        this.fullMessage = StringUtils.concatWithIndentation(actualPath.getPath() + ": ", message);
    }

    public ActualPath getActualPath() {
        return actualPath;
    }

    public String getMessage() {
        return message;
    }

    public String fullMessage() {
        return fullMessage;
    }

    @Override
    public String toString() {
        return fullMessage();
    }
}
