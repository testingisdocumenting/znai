package com.twosigma.documentation.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search and indexing is done by Lunr (http://lunrjs.com/).
 * Prepares Lunr statements and generate javascript to include in each page.
 *
 * @author mykola
 */
public class LunrIndex {
    private List<String> indexStatements;

    public LunrIndex() {
        indexStatements = new ArrayList<>();
    }

    public void addTitle(String id, String idToDisplay, String title) {
        addIndex(id, idToDisplay, "title", title);
    }

    public void addHeader(String id, String idToDisplay, String header) {
        addIndex(id, idToDisplay, "header", header);
    }

    public void addCode(String id, String idToDisplay, String code) {
        addIndex(id, idToDisplay, "code", code);
    }

    public void addBody(String id, String idToDisplay, String body) {
        addIndex(id, idToDisplay, "body", body);
    }

    private void addIndex(String id, String idToDisplay, String type, String text) {
        String lunrAddStatement = "lunrIdx.add({id: '" + idToDisplay + "', " +
            type + ": " + escape(text) + "});\n";

        String titleMapStatement = "lunrRefMap[" + escape(idToDisplay) + "] = " + escape(id) + ";\n";
        indexStatements.add(lunrAddStatement + titleMapStatement);
    }

    private String escape(final String body) {
        final String[] lines = body.replaceAll("\r", "").split("\n");
        return Arrays.stream(lines).map(l -> "'" + escapeSpecialChars(l) + "'").collect(Collectors.joining(" + \n"));
    }

    private String escapeSpecialChars(final String line) {
        return line.replaceAll("'", "\\\\'");
    }

    public String generateJavaScript() {
        return lunrInit() +
            titleToRefMappingInit() +
            indexStatements.stream().collect(Collectors.joining("\n"));
    }

    private String lunrInit() {
        return "lunrIdx = lunr(function() {\n" +
            "  this.field('title', {boost: 15});\n" +
            "  this.field('header', {boost: 10});\n" +
            "  this.field('code', {boost: 5});\n" +
            "  this.field('body');\n" +
            "  this.ref('id'); });\n";
    }

    private String titleToRefMappingInit() {
        return "lunrRefMap = {};";
    }
}
