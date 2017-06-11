package com.twosigma.testing.standalone

import java.nio.file.Path

import static com.twosigma.testing.standalone.StandaloneTestStatus.Errored
import static com.twosigma.testing.standalone.StandaloneTestStatus.Failed
import static com.twosigma.testing.standalone.StandaloneTestStatus.Passed

/**
 * Most of the testing API can be used outside standard JUnit/TestNG setup.
 * One way is to define a simple script. TODO refer example here
 * @author mykola
 */
class StandaloneTest {
    private static StandaloneTestIdGenerator idGenerator = new StandaloneTestIdGenerator()

    private String id
    private Path filePath
    private String description
    private Closure code

    private Throwable exception
    private String assertionMessage

    private List<StandaloneTestResultPayload> payloads

    StandaloneTest(Path filePath, String description, Closure code) {
        this.id = idGenerator.generate(filePath)
        this.filePath = filePath
        this.description = description
        this.code = code
        this.payloads = []
    }

    Path getFilePath() {
        return filePath
    }

    boolean isPassed() {
        return !hasError() && !isFailed()
    }

    boolean hasError() {
        return exception != null && assertionMessage == null
    }

    boolean isFailed() {
        return assertionMessage != null
    }

    StandaloneTestStatus getStatus() {
        if (hasError()) {
            return Errored
        }

        if (isFailed()) {
            return Failed
        }

        return Passed
    }

    void addResultPayload(StandaloneTestResultPayload payload) {
        payloads.add(payload)
    }

    Map<String, ?> toMap() {
        def testAsMap = [id: id,
                         scenario: description,
                         fileName: filePath.fileName.toString(),
                         status: getStatus().toString()]

        payloads.each { testAsMap << it.toMap() }

        return testAsMap
    }

    void run() {
        try {
            code.run()
        } catch (AssertionError e) {
            exception = e
            assertionMessage = e.message
        } catch (Throwable e) {
            exception = e
        }
    }
}
