package com.twosigma.documentation.diagrams.markdown

import com.twosigma.documentation.WebSiteComponentsRegistry
import com.twosigma.documentation.extensions.diagrams.Graphviz
import com.twosigma.documentation.extensions.diagrams.MarkupDiagramSlides
import com.twosigma.documentation.parser.MarkdownParser
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

        def engine = Graphviz.graphvizEngine

        Map<String, Object> diagramPresentation = new LinkedHashMap<>()


        def components = new WebSiteComponentsRegistry()
        def parser = new MarkdownParser(components)
        components.setParser(parser)

        diagramPresentation.put("slides", new MarkupDiagramSlides(parser).create(markdown).toListOfMaps())
        diagramPresentation.put("diagram", engine.diagramFromGv("demo", graphviz))

        String testData = "const data = " + JsonUtils.serializePrettyPrint(diagramPresentation) + "\n\nexport default data"
        Files.write(Paths.get("/Users/mykola/work/testing-documenting/td-documentation-reactjs/src/doc-elements/DiagramSlidesTestData.js"), testData.getBytes())
    }
}
