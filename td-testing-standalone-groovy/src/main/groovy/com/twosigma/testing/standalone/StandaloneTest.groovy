package com.twosigma.testing.standalone

import java.nio.file.Path

/**
 * Most of the testing API can be used outside standard JUnit/TestNG setup.
 * One way is to define a simple script. TODO refer example here
 * @author mykola
 */
class StandaloneTest {
    Path filePath
    String description
    Closure code

    Throwable exception
    String assertionMessage

    StandaloneTest(Path filePath, String description, Closure code) {
        this.filePath = filePath
        this.description = description
        this.code = code
    }

    boolean isPassed() {
        return !hasError() && !isFailed()
    }

    boolean hasError() {
        return exception != null
    }

    boolean isFailed() {
        return assertionMessage != null
    }

    void run() {
        try {
            code.run()
        } catch (AssertionError e) {
            assertionMessage = e.message
        } catch (Throwable e) {
            exception = e
        }
    }
}
