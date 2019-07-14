package com.twosigma.documentation.search;

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

@JacksonXmlRootElement(localName = "mdoc")
public class GlobalSearchEntries {
    private Set<GlobalSearchEntry> entries;

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
