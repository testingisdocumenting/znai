package com.twosigma.documentation.extensions.diagrams.markdown;

import com.twosigma.documentation.extensions.diagrams.slides.DiagramSlides;
import com.twosigma.documentation.parser.MarkdownParser;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.util.ArrayList;
import java.util.List;

import static com.twosigma.documentation.parser.docelement.DocElementType.SECTION;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class MarkdownDiagramSlides {
    private static final MarkdownParser parser = new MarkdownParser(); // TODO DI?

    private DiagramSlides diagramSlides;
    private String markdownContent;
    private List<DocElement> sections;
    private List<String> currentIds;

    private MarkdownDiagramSlides(String markdownContent) {
        this.markdownContent = markdownContent;
        this.currentIds = new ArrayList<>();
        this.diagramSlides = new DiagramSlides();
    }

    public static DiagramSlides createSlides(String markdownContent) {
        return new MarkdownDiagramSlides(markdownContent).create();
    }

    public DiagramSlides create() {
        parse();
        sections.forEach(this::convert);

        return diagramSlides;
    }

    private void parse() {
        DocElement page = parser.parse(markdownContent);
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
