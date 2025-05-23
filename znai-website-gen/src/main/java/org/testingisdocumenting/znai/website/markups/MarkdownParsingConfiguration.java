/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.website.markups;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParsingConfiguration;
import org.testingisdocumenting.znai.parser.MarkupTypes;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.structure.PlainTextTocGenerator;
import org.testingisdocumenting.znai.structure.PlainTextTocPatcher;
import org.testingisdocumenting.znai.structure.TableOfContents;
import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.FilePathUtils;

import java.nio.file.Path;

public class MarkdownParsingConfiguration implements MarkupParsingConfiguration {
    public static final String TOC_PATCH_NAME = "toc-patch";

    @Override
    public String configurationName() {
        return MarkupTypes.MARKDOWN;
    }

    @Override
    public TableOfContents createToc(String docTitle, ComponentsRegistry componentsRegistry) {
        TableOfContents toc = new PlainTextTocGenerator(filesExtension()).generate(
                componentsRegistry.resourceResolver().textContent("toc"));
        toc.addIndex();

        if (componentsRegistry.resourceResolver().canResolve(TOC_PATCH_NAME)) {
            String patch = componentsRegistry.resourceResolver().textContent(TOC_PATCH_NAME);
            new PlainTextTocPatcher(toc).patch(patch);
        }

        return toc;
    }

    @Override
    public MarkupParser createMarkupParser(ComponentsRegistry componentsRegistry) {
        return new MarkdownParser(componentsRegistry);
    }

    @Override
    public String tocItemResourceName(TocItem tocItem) {
        if (tocItem.isIndex()) {
            return "index.md";
        }

        return tocItem.getFilePath();
    }

    @Override
    public Path fullPath(ComponentsRegistry componentsRegistry, Path root, TocItem tocItem) {
        return componentsRegistry.resourceResolver().fullPath(tocItemResourceName(tocItem));
    }

    @Override
    public TocItem tocItemByPath(ComponentsRegistry componentsRegistry, TableOfContents toc, Path path) {
        TocItem tocItem = toc.findTocItem(path);

        if (tocItem != null) {
            return tocItem;
        }

        String fileNameWithoutExtension = FilePathUtils.fileNameWithoutExtension(path);
        if (fileNameWithoutExtension.equals("index")) {
            return toc.getIndex();
        }

        return null;
    }

    private String filesExtension() {
        return "md";
    }
}
