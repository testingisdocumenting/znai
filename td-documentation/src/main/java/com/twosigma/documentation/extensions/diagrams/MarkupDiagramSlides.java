package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.documentation.extensions.diagrams.slides.DiagramSlides;
import com.twosigma.documentation.extensions.include.RelativeToFileAndRootResourceResolver;
import com.twosigma.documentation.parser.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.twosigma.documentation.parser.docelement.DocElementType.SECTION;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class MarkupDiagramSlides {
    private DiagramSlides diagramSlides;
    private List<DocElement> sections;
    private List<String> currentIds;
    private MarkupParser parser;

    public MarkupDiagramSlides(MarkupParser parser) {
        this.parser = parser;
    }

    public DiagramSlides create(String markupContent) {
        parse(markupContent);

        this.currentIds = new ArrayList<>();
        this.diagramSlides = new DiagramSlides();
        sections.forEach(this::convert);

        return diagramSlides;
    }

    private void parse(String markupContent) {
        DocElement page = parser.parse(markupContent);
        sections = page.getContent().stream().
                filter(e -> e.getType().equals(SECTION)).collect(toList());
    }

    private void convert(DocElement section) {
        currentIds.add(section.getProp("title").toString());
        if (! section.getContent().isEmpty()) {
            diagramSlides.add(currentIds, section.getContent());
            currentIds = new ArrayList<>();
        }
    }
}
