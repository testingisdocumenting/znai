package com.twosigma.documentation.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static freemarker.template.Configuration.VERSION_2_3_23;

/**
 * @author mykola
 */
public class TextTemplate {
    private final Template template;

    public TextTemplate(String templateText) {
        Configuration cfg = new Configuration(VERSION_2_3_23);
        try {
            template = new Template("templateName", new StringReader(templateText), cfg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String process(Map<String, Object> model) {
        StringWriter out = new StringWriter();
        try {
            template.process(model, out);
            return out.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
