/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@JacksonXmlRootElement(localName = "znai")
public class GlobalSearchEntries {
    private final Set<GlobalSearchEntry> entries;

    public GlobalSearchEntries() {
        entries = new LinkedHashSet<>();
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entry")
    public Set<GlobalSearchEntry> getEntries() {
        return entries;
    }

    public void addAll(List<GlobalSearchEntry> entries) {
        this.entries.addAll(entries);
    }

    public String toXml() {
        ObjectMapper objectMapper = new XmlMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
