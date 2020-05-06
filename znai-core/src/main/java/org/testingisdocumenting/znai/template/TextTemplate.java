/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static freemarker.template.Configuration.VERSION_2_3_23;

public class TextTemplate {
    private final Template template;

    public TextTemplate(String templateName, String templateText) {
        Configuration cfg = new Configuration(VERSION_2_3_23);
        try {
            template = new Template(templateName, new StringReader(templateText), cfg);
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
