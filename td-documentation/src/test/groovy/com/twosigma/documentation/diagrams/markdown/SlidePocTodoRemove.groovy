package com.twosigma.documentation.diagrams.markdown

import com.twosigma.diagrams.graphviz.GraphvizEngine
import com.twosigma.diagrams.graphviz.InteractiveCmdGraphviz
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig
import com.twosigma.documentation.extensions.diagrams.markdown.MarkdownDiagramSlides
import com.twosigma.utils.FileUtils
import com.twosigma.utils.JsonUtils
import com.twosigma.utils.ResourceUtils

import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author mykola
 */
class SlidePocTodoRemove {
    static void main(String[] args) {
        def graphviz = ResourceUtils.textContent("test.gv")
        def markdown = ResourceUtils.textContent("test-flow.md")

        def shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-meta-conf.json"))
        def runtime = new InteractiveCmdGraphviz()
        def engine = new GraphvizEngine(runtime, shapeConfig)


        Map<String, Object> diagramPresentation = new LinkedHashMap<>()

        diagramPresentation.put("slides", MarkdownDiagramSlides.createSlides(markdown).toListOfMaps())
        diagramPresentation.put("diagram", engine.diagramFromGv(graphviz))

        String testData = "const data = " + JsonUtils.serializePrettyPrint(diagramPresentation) + "\n\nexport default data"
        Files.write(Paths.get("/Users/mykola/work/testing-documenting/td-documentation-reactjs/src/doc-elements/DiagramSlidesTestData.js"), testData.getBytes())
    }
}
