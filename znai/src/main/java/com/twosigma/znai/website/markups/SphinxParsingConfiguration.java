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
import com.twosigma.znai.parser.sphinx.DocTreeTocGenerator;
import com.twosigma.znai.parser.sphinx.SphinxDocTreeParser;
import com.twosigma.znai.structure.TableOfContents;
import com.twosigma.znai.structure.TocItem;

import java.nio.file.Path;

import static com.twosigma.utils.FileUtils.fileTextContent;

public class SphinxParsingConfiguration implements MarkupParsingConfiguration {
    @Override
    public TableOfContents createToc(Path tocPath) {
        return new DocTreeTocGenerator(filesExtension()).generate(fileTextContent(tocPath));
    }

    @Override
    public MarkupParser createMarkupParser(ComponentsRegistry componentsRegistry) {
        return new SphinxDocTreeParser(componentsRegistry);
    }

    @Override
    public String filesExtension() {
        return "xml";
    }

    @Override
    public Path fullPath(Path root, TocItem tocItem) {
        return root.resolve(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension() + "." + filesExtension());
    }
}
