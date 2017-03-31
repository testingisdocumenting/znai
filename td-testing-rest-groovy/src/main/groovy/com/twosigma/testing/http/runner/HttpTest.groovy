package com.twosigma.testing.http.runner

import com.twosigma.testing.http.HttpValidationResult
import com.twosigma.testing.http.datacoverage.DataNodeToMapWithChecksConverter
import com.twosigma.testing.http.json.JsonSerialization
import com.twosigma.testing.http.render.DataNodeRenderer

import java.nio.file.Paths

/**
 * @author mykola
 */
class HttpTest {
    private String testName
    private Script script

    private int passedCalls
    private int failedCalls
    private int callNumber

    HttpTest(final String testName, final Script script) {
        this.testName = testName
        this.script = script
    }

    void run() {
        // TODO console with colors
        println testName
        println ""

        script.run()
    }

    void afterValidation(final HttpValidationResult validationResult) {
        callNumber++

        def passed = validationResult.mismatches.isEmpty()
        if (passed) {
            passedCalls++
        } else {
            failedCalls++
        }

        def passFailPrefix = passed ? "[.]" : "[X]"
        println "$passFailPrefix ${validationResult.requestMethod} : ${validationResult.fullUrl}"

        println validationResult.mismatches.join("\n")
        println ""
        println DataNodeRenderer.render(validationResult.body)

        def bodyWithChecksAsMap = DataNodeToMapWithChecksConverter.convert(validationResult.body)
        def json = JsonSerialization.toJson(bodyWithChecksAsMap)

        println bodyWithChecksAsMap
        Paths.get(testName + "-" + callNumber + ".json").toFile().text = json
    }
}
