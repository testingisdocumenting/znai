package com.twosigma.znai.parser.sphinx.xml;

import com.twosigma.utils.RegexpUtils;

import java.util.regex.Pattern;

public class DocUtilsXmlFixer {
    private static final Pattern DESC_SIGNATURE_INVALID_XML_TAG_PATTERN =
            Pattern.compile("<desc_signature\\s*([^>]+)module\\s*([^=])([^>]+)>");

    private DocUtilsXmlFixer() {
    }

    public static String fixDocUtilsIncorrectXml(String xml) {
        return RegexpUtils.replaceAll(xml, DESC_SIGNATURE_INVALID_XML_TAG_PATTERN,
                (m) -> "<desc_signature " + m.group(1) + " module=\"\" " + m.group(2) + m.group(3) + ">");
    }
}
