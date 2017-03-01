package com.twosigma.documentation.parser;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludePluginResult;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class DummyIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "dummy";
    }

    @Override
    public IncludePluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        DocElement dummy = new DocElement("Dummy");
        dummy.addProp("ff", includeParams.getFreeParam());
        dummy.addProp("opts", includeParams.getOpts().toMap());

        return IncludePluginResult.docElements(Stream.of(dummy));
    }
}
