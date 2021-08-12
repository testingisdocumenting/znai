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
import org.testingisdocumenting.znai.parser.sphinx.DocTreeTocGenerator;
import org.testingisdocumenting.znai.parser.sphinx.SphinxDocTreeParser;
import org.testingisdocumenting.znai.structure.TableOfContents;
import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.FilePathUtils;

import java.nio.file.Path;

public class SphinxParsingConfiguration implements MarkupParsingConfiguration {
    @Override
    public String configurationName() {
        return MarkupTypes.SPHINX;
    }

    @Override
    public TableOfContents createToc(String docTitle, ComponentsRegistry componentsRegistry) {
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
        return toc.findTocItem(path);
    }

    private String filesExtension() {
        return "xml";
    }
}
