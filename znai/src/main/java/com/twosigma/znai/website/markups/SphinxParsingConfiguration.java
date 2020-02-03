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
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParsingConfiguration;
import com.twosigma.znai.parser.MarkupTypes;
import com.twosigma.znai.parser.sphinx.DocTreeTocGenerator;
import com.twosigma.znai.parser.sphinx.SphinxDocTreeParser;
import com.twosigma.znai.structure.TableOfContents;
import com.twosigma.znai.structure.TocItem;
import com.twosigma.znai.utils.FilePathUtils;

import java.nio.file.Path;

public class SphinxParsingConfiguration implements MarkupParsingConfiguration {
    @Override
    public String configurationName() {
        return MarkupTypes.SPHINX;
    }

    @Override
    public TableOfContents createToc(ComponentsRegistry componentsRegistry) {
        return new DocTreeTocGenerator().generate(
                componentsRegistry.resourceResolver().textContent("index.xml"));
    }

    @Override
    public MarkupParser createMarkupParser(ComponentsRegistry componentsRegistry) {
        return new SphinxDocTreeParser(componentsRegistry);
    }

    @Override
    public String tocItemResourceName(TocItem tocItem) {
        return tocItem.getFileNameWithoutExtension() + "." + filesExtension();
    }

    @Override
    public Path fullPath(ComponentsRegistry componentsRegistry, Path root, TocItem tocItem) {
        return root.resolve(tocItem.getDirName()).resolve(tocItemResourceName(tocItem));
    }

    @Override
    public TocItem tocItemByPath(ComponentsRegistry componentsRegistry, TableOfContents toc, Path path) {
        if (path.getFileName().toString().startsWith(TocItem.INDEX + ".")) {
            return toc.getIndex();
        }

        return toc.findTocItem(path.toAbsolutePath().getParent().getFileName().toString(),
                FilePathUtils.fileNameWithoutExtension(path));
    }

    private String filesExtension() {
        return "xml";
    }
}
