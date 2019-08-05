/*
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

package com.twosigma.znai.website.markups;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.parser.commonmark.MarkdownParser;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.structure.PlainTextTocGenerator;
import com.twosigma.znai.structure.PlainTextTocPatcher;
import com.twosigma.znai.structure.TableOfContents;
import com.twosigma.znai.structure.TocItem;

import java.io.File;
import java.nio.file.Path;

public class MarkdownParsingConfiguration implements MarkupParsingConfiguration {
    public static final String TOC_PATCH_NAME = "toc-patch";

    @Override
    public TableOfContents createToc(ComponentsRegistry componentsRegistry) {
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
    public String filesExtension() {
        return "md";
    }

    @Override
    public Path fullPath(ComponentsRegistry componentsRegistry, Path root, TocItem tocItem) {
        return componentsRegistry.resourceResolver().fullPath(tocItem.getDirName()
                 + (tocItem.getDirName().isEmpty() ? "" : File.separator) +
                (tocItem.getFileNameWithoutExtension() + "." + filesExtension()));
    }
}
