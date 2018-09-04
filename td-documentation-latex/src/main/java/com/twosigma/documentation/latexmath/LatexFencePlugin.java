package com.twosigma.documentation.latexmath;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;

import java.nio.file.Path;
import java.util.Collections;

/**
 * @author mykola
 */
public class LatexFencePlugin implements FencePlugin {
    private String content;

    @Override
    public String id() {
        return "latex";
    }

    @Override
    public FencePlugin create() {
        return new LatexFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.content = content;
        return PluginResult.docElement("Latex", Collections.singletonMap("latex", content));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(this.content);
    }
}
