package com.twosigma.documentation.jupyter;

import java.util.List;

public class JupyterNotebook {
    private final String lang;
    private final List<JupyterCell> cells;

    public JupyterNotebook(String lang, List<JupyterCell> cells) {
        this.lang = lang;
        this.cells = cells;
    }

    public String getLang() {
        return lang;
    }

    public List<JupyterCell> getCells() {
        return cells;
    }
}
