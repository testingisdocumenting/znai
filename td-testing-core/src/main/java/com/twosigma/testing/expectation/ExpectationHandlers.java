package com.twosigma.testing.expectation;

import java.util.List;

import com.twosigma.testing.expectation.ExpectationHandler.Flow;
import com.twosigma.utils.ServiceUtils;

/**
 * @author mykola
 */
public class ExpectationHandlers {
    private static List<ExpectationHandler> handlers = ServiceUtils.discover(ExpectationHandler.class);

    public static void add(ExpectationHandler handler) {
        handlers.add(handler);
    }

    public static void remove(ExpectationHandler handler) {
        handlers.remove(handler);
    }

    public static Flow onValueMismatch(ActualPath actualPath, Object actualValue, String message) {
        for (ExpectationHandler handler : handlers) {
            final Flow flow = handler.onValueMismatch(actualPath, actualValue, message);
            if (flow == Flow.Terminate) {
                return Flow.Terminate;
            }
        }

        return Flow.PassToNext;
    }

}
