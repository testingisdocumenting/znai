package com.twosigma.testing.http.runner

import com.twosigma.testing.documentation.DocumentationContext
import com.twosigma.testing.http.HttpValidationResult
import com.twosigma.testing.http.datacoverage.DataNodeToMapOfValuesConverter
import com.twosigma.testing.http.render.DataNodeRenderer
import com.twosigma.utils.JsonUtils

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

    private List<HttpValidationResult> httpValidationResults

    HttpTest(String testName, Script script) {
        this.testName = testName
        this.script = script
        this.httpValidationResults = []
    }

    void run() {
        // TODO console with colors
        println testName
        println ""

        DocumentationContext.reset()
        script.run()

        createDocumentationArtifact()
    }

    void afterValidation(HttpValidationResult validationResult) {
        callNumber++

        def passed = validationResult.mismatches.isEmpty()
        if (passed) {
            passedCalls++
        } else {
            failedCalls++
        }

        println DocumentationContext.markupScenario

        def passFailPrefix = passed ? "[.]" : "[X]"
        println "$passFailPrefix ${validationResult.requestMethod} : ${validationResult.fullUrl}"

        println validationResult.mismatches.join("\n")
        println ""
        println DataNodeRenderer.render(validationResult.body)

        httpValidationResults.add(validationResult)
    }

    private void createDocumentationArtifact() {
        def results = httpValidationResults.collect this.&validationResultToJson
        def payload = [
                scenario: DocumentationContext.markupScenario,
                results: results]

        println payload

        Paths.get(testName + ".json").toFile().text = JsonUtils.serialize(payload)
    }

    private static Map<?, ?> validationResultToJson(HttpValidationResult r) {
        def pathsToHighlight = []
        def traceableValueConverter = { id, traceableValue -> pathsToHighlight.add(id.getPath()); return traceableValue.value }
        def dataNodeConverter = new DataNodeToMapOfValuesConverter(traceableValueConverter)
        def convertedBody = dataNodeConverter.convert(r.body)

        return [id: r.requestMethod.toUpperCase() + ":" + r.url,
                url: r.url,
                body: convertedBody,
                paths: pathsToHighlight]
    }
}
