package com.twosigma.znai.jupyter;

import java.util.List;

public class JupyterCell {
    public static final String CODE_TYPE = "code";
    public static final String MARKDOWN_TYPE = "markdown";

    private final String type;
    private final String input;
    private final List<JupyterOutput> outputs;

    public JupyterCell(String type, String input, List<JupyterOutput> outputs) {
        this.type = type;
        this.input = input;
        this.outputs = outputs;
    }

    public String getType() {
        return type;
    }

    public String getInput() {
        return input;
    }

    public List<JupyterOutput> getOutputs() {
        return outputs;
    }
}
