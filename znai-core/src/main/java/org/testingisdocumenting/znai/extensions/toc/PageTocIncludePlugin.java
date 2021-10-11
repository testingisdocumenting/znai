/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions.toc;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.PageSectionIdTitle;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.structure.TableOfContents;
import org.testingisdocumenting.znai.structure.TocItem;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PageTocIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "page-toc";
    }

    @Override
    public IncludePlugin create() {
        return new PageTocIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        TableOfContents toc = componentsRegistry.docStructure().tableOfContents();

        TocItem tocItem = toc.findTocItem(markupPath);
        if (tocItem == null) {
            throw new IllegalStateException("File is not part of TOC: " + markupPath.toAbsolutePath());
        }

        Map<String, Object> props = new LinkedHashMap<>();
        Supplier<?> buildSectionsSupplier = () -> buildSections(tocItem);
        props.put("sections", buildSectionsSupplier);

        return PluginResult.docElement("PageToc", props);
    }

    private List<Map<String, ?>> buildSections(TocItem tocItem) {
        return tocItem.getPageSectionIdTitles().stream()
                .map(PageSectionIdTitle::toMap)
                .collect(Collectors.toList());
    }
}
