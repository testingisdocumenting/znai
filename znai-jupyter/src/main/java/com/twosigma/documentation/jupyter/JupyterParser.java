package com.twosigma.documentation.jupyter;

import java.util.List;
import java.util.Map;

public interface JupyterParser {
    JupyterNotebook parse(Map<String, ?> json);
}
