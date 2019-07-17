package com.twosigma.znai.java.extensions;

import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.java.parser.EnumEntry;
import com.twosigma.znai.java.parser.JavaCode;
import com.twosigma.znai.java.parser.html.HtmlToDocElementConverter;
import com.twosigma.znai.parser.docelement.DocElement;
import com.twosigma.znai.parser.docelement.DocElementType;
import com.twosigma.utils.CollectionUtils;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class JavaEnumEntriesIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-enum-entries";
    }

    @Override
    public IncludePlugin create() {
        return new JavaEnumEntriesIncludePlugin();
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        List<List<?>> data = javaCode.getEnumEntries().stream()
                .filter(this::includeEnum)
                .map(e -> Arrays.asList(nameToDocElements(e), descriptionToDocElements(e)))
                .collect(toList());

        List<Map<String, String>> columns = new ArrayList<>();
        columns.add(CollectionUtils.createMap("title", "name",
                "align", "right",
                "width", "20%"));

        columns.add(CollectionUtils.createMap("title", "description"));

        Map<Object, Object> tableProps = new LinkedHashMap<>();
        tableProps.put("data", data);
        tableProps.put("columns", columns);
        tableProps.put("styles", Arrays.asList("middle-vertical-lines-only", "no-header", "no-vertical-padding"));

        DocElement table = new DocElement(DocElementType.TABLE, "table", tableProps);
        return new JavaIncludeResult(Collections.singletonList(table), extractText(javaCode.getEnumEntries())) ;
    }

    private boolean includeEnum(EnumEntry enumEntry) {
        boolean excludeDeprecated = pluginParams.getOpts().get("excludeDeprecated", false);
        return !(excludeDeprecated && enumEntry.isDeprecated());
    }

    private String extractText(List<EnumEntry> enums) {
        return enums.stream().map(e -> e.getName() + " " + e.getJavaDocText())
                .collect(joining(" "));
    }

    private List<Map<String, Object>> nameToDocElements(EnumEntry e) {
        return Collections.singletonList(
                new DocElement(DocElementType.INLINED_CODE, "code", e.getName()).toMap());
    }

    private List<Map<String, Object>> descriptionToDocElements(EnumEntry e) {
        return HtmlToDocElementConverter.convert(componentsRegistry, markupPath, e.getJavaDocText())
                .stream()
                .map(DocElement::toMap).collect(toList());
    }
}
