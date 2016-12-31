package com.twosigma.documentation.extensions;

import java.util.Map;

/**
 * @author mykola
 */
public class ReactComponent {
    private String name;
    private Map<String, Object> props;

    public ReactComponent(final String name, final Map<String, Object> props) {
        this.name = name;
        this.props = props;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getProps() {
        return props;
    }
}
