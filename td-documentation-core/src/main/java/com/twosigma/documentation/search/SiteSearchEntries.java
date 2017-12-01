package com.twosigma.documentation.search;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
@XmlRootElement(name = "mdoc")
public class SiteSearchEntries {
    private static final JAXBContext jaxbContext = createJaxbContext();

    private List<SiteSearchEntry> entries;

    public SiteSearchEntries() {
        entries = new ArrayList<>();
    }

    @XmlElement(name = "entry")
    public List<SiteSearchEntry> getEntries() {
        return entries;
    }

    public void addAll(List<SiteSearchEntry> entries) {
        this.entries.addAll(entries);
    }

    public String toXml() {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Writer writer = new StringWriter();
            marshaller.marshal(this, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(SiteSearchEntries.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
