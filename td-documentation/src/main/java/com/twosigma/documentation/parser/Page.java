package com.twosigma.documentation.parser;

import java.util.List;
import java.util.stream.Stream;

import com.twosigma.documentation.parser.docelement.DocElement;

import static com.twosigma.documentation.parser.docelement.DocElementType.SECTION;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class Page {
    private final DocElement docElement;
    private final List<String> firstLevelHeadings;

    public Page(final DocElement docElement) {
        this.docElement = docElement;
        this.firstLevelHeadings = extractFirstLevelHeadings(docElement);
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public Stream<String> getFirstLevelHeadings() {
        return firstLevelHeadings.stream();
    }

    private List<String> extractFirstLevelHeadings(final DocElement docElement) {
        return docElement.getContent().stream().
            filter(e -> e.getType().equals(SECTION)).
            map(e -> e.getProp("title").toString()).
            collect(toList());
    }
}
