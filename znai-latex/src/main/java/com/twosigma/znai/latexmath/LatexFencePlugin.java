package com.twosigma.znai.latexmath;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Collections;

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
