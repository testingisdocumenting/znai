package com.twosigma.documentation.diagrams.slides;

import com.twosigma.documentation.parser.docelement.DocElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class DiagramSlides {
    private final List<DiagramSlide> slides;

    public DiagramSlides() {
        slides = new ArrayList<>();
    }

    public void add(List<String> ids, List<DocElement> content) {
        slides.add(new DiagramSlide(ids, content));
    }

    public List<DiagramSlide> getSlides() {
        return Collections.unmodifiableList(slides);
    }

    public List<Map<String, Object>> toListOfMaps() {
        return slides.stream().map(DiagramSlide::toMap).collect(toList());
    }
}