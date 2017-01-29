package com.twosigma.documentation.structure;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class TableOfContents {
    private List<TocItem> tocItems;

    private TableOfContents() {
        tocItems = new ArrayList<>();
    }

    public static TableOfContents fromNestedText(String nestedText) {
        return new Parser(nestedText).parse();
    }

    public void addTocItem(final String sectionName, final String fileNameWithoutExtension) {
        TocItem tocItem = new TocItem(sectionName, fileNameWithoutExtension);

        if (! tocItems.isEmpty()) {
            final TocItem lastTocItem = tocItems.get(tocItems.size() - 1);
            lastTocItem.setNext(tocItem);
            tocItem.setPrev(lastTocItem);
        }

        tocItems.add(tocItem);
    }

    public List<TocItem> getTocItems() {
        return Collections.unmodifiableList(tocItems);
    }

    public List<Map<String, Object>> toListOfMaps() {
        Map<String, List<TocItem>> bySectionTitle = tocItems.stream().collect(
                groupingBy(TocItem::getSectionTitle, LinkedHashMap::new, toList()));

        List<Map<String, Object>> result = new ArrayList<>();
        bySectionTitle.forEach((sectionTitle, items) -> result.add(createSectionWithItems(sectionTitle, items)));

        return result;
    }

    private Map<String, Object> createSectionWithItems(String sectionTitle, List<TocItem> items) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (items.isEmpty()) {
            return result;
        }

        result.put("sectionTitle", sectionTitle);
        result.put("dirName", items.iterator().next().getDirName());
        result.put("items", items.stream().map(TocItem::toMap).collect(toList()));

        return result;
    }

    private static class Parser {
        public static final String INDENTATION = "    ";
        private List<String> nestedLines;
        private String currentSection;
        private TableOfContents toc;

        public Parser(final String nestedText) {
            nestedLines = Arrays.asList(nestedText.replace("\r", "").split("\n"));
            toc = new TableOfContents();
        }

        public TableOfContents parse() {
            nestedLines.forEach(this::parse);
            return toc;
        }

        private void parse(final String line) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty())
                return;

            if (line.startsWith(INDENTATION)) {
                handlePageEntry(trimmedLine);
            } else if (line.startsWith(" ")) {
                handleSyntaxError();
            } else {
                handleSectionEntry(trimmedLine);
            }
        }

        private void handleSyntaxError() {
            throw new IllegalArgumentException(
                "toc line should either start with " + INDENTATION.length() + " spaces to denote " +
                    "section name, or start without spaces to denote page file name");
        }

        private void handleSectionEntry(final String trimmedLine) {
            currentSection = trimmedLine;
        }

        private void handlePageEntry(final String line) {
            if (currentSection == null) {
                throw new IllegalArgumentException(
                    "section is not specified, use a line without indentation to specify a section");
            } else {
                toc.addTocItem(currentSection, line);
            }
        }
    }
}
