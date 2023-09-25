/*
 * Copyright 2020 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.extensions.html;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class IframeIncludePlugin implements IncludePlugin {
    private String userUrl;

    @Override
    public String id() {
        return "iframe";
    }

    @Override
    public IncludePlugin create() {
        return new IframeIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        DocElement iframe = new DocElement("Iframe");
        userUrl = pluginParams.getFreeParam();
        String urlToUse = UrlUtils.isExternal(userUrl) ? userUrl :
                componentsRegistry.docStructure().fullUrl(userUrl);
        iframe.addProp("src", urlToUse);
        pluginParams.getOpts().assignToDocElement(iframe);

        return PluginResult.docElement(iframe);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        if (UrlUtils.isExternal(userUrl)) {
            return Stream.empty();
        }

        var withoutAnchor = UrlUtils.removeAnchor(userUrl);
        var withIndexHtmlIfRequired = UrlUtils.attachIndexHtmlIfEndsWithSlash(withoutAnchor);

        return Stream.of(AuxiliaryFile.runTime(componentsRegistry.resourceResolver().fullPath(withIndexHtmlIfRequired),
                Paths.get(withIndexHtmlIfRequired)));
    }
}
