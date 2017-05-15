package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.java.parser.JavaMethod;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.template.TextTemplate;
import com.twosigma.utils.CollectionUtils;
import com.twosigma.utils.ResourceUtils;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class JavaDocParamsIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-doc-params";
    }

    @Override
    public List<DocElement> process(JavaCode javaCode) {
        JavaMethod javaMethod = javaCode.methodByName(entry);
        List<Map<Object, Object>> params = javaMethod.getParams().stream().map(p -> CollectionUtils.createMap("name", p.getName(),
                "description", p.getJavaDocText(),
                "type", p.getType())).collect(toList());

        TextTemplate textTemplate = new TextTemplate(ResourceUtils.textContent("templates/javaDocParams.md"));

        MarkupParserResult parserResult = componentsRegistry.parser().parse(markupPath,
                textTemplate.process(CollectionUtils.createMap("params", params)));

        return parserResult.getDocElement().getContent();
    }
}
