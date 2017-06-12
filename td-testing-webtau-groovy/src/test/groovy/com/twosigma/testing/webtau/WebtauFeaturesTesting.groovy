package com.twosigma.testing.webtau

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.Color
import com.twosigma.testing.WebTauFeaturesTestServer
import com.twosigma.testing.reporter.StepReporter
import com.twosigma.testing.reporter.StepReporters
import com.twosigma.testing.reporter.TestStep
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestListeners
import com.twosigma.testing.webtau.cli.WebUiTestCliApp
import com.twosigma.utils.FileUtils
import com.twosigma.utils.JsonUtils
import com.twosigma.utils.ResourceUtils
import com.twosigma.utils.StringUtils
import org.jsoup.Jsoup
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class WebtauFeaturesTesting implements StepReporter, StandaloneTestListener {
    private static final int testServerPort = 8180
    private static WebTauFeaturesTestServer testServer

    private List<String> report = new ArrayList<>()
    private String testDescription

    @BeforeClass
    static void init() {
        System.setProperty("url", "http://localhost:" + testServerPort)

        testServer = new WebTauFeaturesTestServer()
        testServer.start(testServerPort)
    }

    @AfterClass
    static void cleanup() {
        testServer.stop()
    }

    @Test
    void "waitTo"() {
        runCli("api/waitTo.groovy")
    }

    @Test
    void "waitToNot"() {
        runCli("api/waitToNot.groovy")
    }

    @Test
    void "should"() {
        runCli("api/should.groovy")
    }

    @Test
    void "shouldNot"() {
        runCli("api/shouldNot.groovy")
    }

    @Test
    void "regexp"() {
        runCli("api/regexp.groovy")
    }

    @Test
    void "save html snippets"() {
        saveSnippet("finders-and-filters.html", "#menu", "menu")
    }

    @Test
    void "filter by text"() {
        runCli("api/byText.groovy")
    }

    @Test
    void "filter by regexp"() {
        runCli("api/byRegexp.groovy")
    }

    @Test
    void "filter by number"() {
        runCli("api/byNumber.groovy")
    }

    @Test
    void "extract script for documentation"() {
        def testPath = Paths.get("examples/api/waitTo.groovy")
        def script = FileUtils.fileTextContent(testPath)

        String scope = extractScenarioBody(script)
        Assert.assertEquals("search.open()\n" +
                "search.submit(query: \"search this\")\n" +
                "search.numberOfResults.waitTo == 2", scope)
    }

    private static void saveSnippet(String resourceName, String css, String snippetOutName) {
        def html = ResourceUtils.textContent(resourceName)

        def snippetPath = Paths.get("test-artifacts/snippets").resolve(snippetOutName + ".html")
        FileUtils.writeTextContent(snippetPath, Jsoup.parse(html).select(css).toString())
    }

    private static String extractScenarioBody(String script) {
        def scopeStartIdx = script.indexOf("{")
        def scopeEndIdx = script.lastIndexOf("}")

        return StringUtils.stripIndentation(script.substring(scopeStartIdx + 1, scopeEndIdx))
    }

    void runCli(String testFileName) {
        def testPath = Paths.get("examples/" + testFileName)
        def script = FileUtils.fileTextContent(testPath)
        def example = extractScenarioBody(script)

        StepReporters.add(this)
        StandaloneTestListeners.add(this)
        report.clear()

        try {
            def cliApp = new WebUiTestCliApp("--url=http://localhost:" + testServerPort, testPath.toString())
            cliApp.start()
        } finally {
            def testArtifact = [description: testDescription, example: example, report: report]

            saveTestArtifact(testFileName, testArtifact)

            StepReporters.remove(this)
            StandaloneTestListeners.remove(this)
        }
    }

    private static void saveTestArtifact(String testFileName, artifact) {
        def json = JsonUtils.serializePrettyPrint(artifact)
        def expected = Paths.get("test-artifacts").resolve(testFileName + ".result")
        def actualPath = Paths.get("test-artifacts").resolve(testFileName + ".result.actual")

        if (! Files.exists(expected)) {
            FileUtils.writeTextContent(expected, json)

            throw new AssertionError("make sure " + expected + " is correct. and it to repo as a baseline. " +
                    "test will not fail next time unless output of the test is changed")
        }

        def expectedReport = JsonUtils.deserialize(FileUtils.fileTextContent(expected)).report


        if (! expectedReport.equals(artifact.report)) {
            ConsoleOutputs.out("reports are different, you can use IDE to compare files: ", Color.PURPLE, actualPath,
                    Color.BLUE, " and ", Color.PURPLE, expected)
            FileUtils.writeTextContent(actualPath, json)
            Assert.assertEquals(expectedReport.join("\n"), artifact.report.join("\n"))
        } else {
            FileUtils.writeTextContent(expected, json)
            Files.deleteIfExists(actualPath)
        }
    }

    @Override
    void onStepStart(TestStep step) {
        def stepAsText = "> " + StringUtils.createIndentation(step.numberOfParents * 2) + step.getInProgressMessage().toString()

        println stepAsText
        report.add(stepAsText)
    }

    @Override
    void onStepSuccess(TestStep step) {
        def stepAsText = ". " + StringUtils.createIndentation(step.numberOfParents * 2)

        println stepAsText
        report.add(stepAsText + step.getCompletionMessage().toString())
    }

    @Override
    void onStepFailure(TestStep step) {
        def stepAsText = "X " + StringUtils.createIndentation(step.numberOfParents * 2) + step.getCompletionMessage().toString()

        println stepAsText
        report.add(stepAsText)
    }

    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeScriptParse(Path scriptPath) {
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        testDescription = test.description
    }

    @Override
    void afterAllTests() {
    }
}
