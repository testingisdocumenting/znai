package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.template.TextTemplate;
import com.twosigma.utils.CollectionUtils;
import com.twosigma.utils.ResourceUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class JavaEnumEntriesIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-enum-entries";
    }

    @Override
    public List<DocElement> process(JavaCode javaCode) {
        List<Map<Object, Object>> entries = javaCode.getEnumEntries().stream().map(e -> CollectionUtils.createMap("name", e.getName(),
                "description", e.getJavaDocText())).collect(toList());

        TextTemplate textTemplate = new TextTemplate("java-enum-entries",
                ResourceUtils.textContent("templates/javaEnumEntries.md"));

        MarkupParserResult parserResult = componentsRegistry.parser().parse(markupPath,
                textTemplate.process(Collections.singletonMap("entries", entries)));

        return parserResult.getDocElement().getContent();
    }
}
