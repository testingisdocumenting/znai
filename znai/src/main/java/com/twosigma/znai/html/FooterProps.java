package com.twosigma.znai.html;

import com.twosigma.znai.structure.Footer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class FooterProps {
    private Footer footer;

    public FooterProps(Footer footer) {
        this.footer = footer;
    }

    public Map<String, ?> toMap() {
        if (footer == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> props = new LinkedHashMap<>();

        props.put("type", "Footer");
        props.put("content", ((Map<String, ?>) footer.getDocElement().toMap()).get("content"));

        return props;
    }
}
