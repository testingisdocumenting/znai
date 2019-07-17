package com.twosigma.znai.jupyter;

import java.util.Map;

public interface JupyterParser {
    JupyterNotebook parse(Map<String, ?> json);
}
