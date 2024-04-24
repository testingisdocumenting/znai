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

package org.testingisdocumenting.znai.diagrams.slides;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MarkupDiagramSlides {
    private DiagramSlides diagramSlides;
    private List<DocElement> sections;
    private List<String> currentIds;
    private MarkupParser parser;
    private MarkupParserResult parserResult;

    public MarkupDiagramSlides(MarkupParser parser) {
        this.parser = parser;
    }

    public DiagramSlides create(Path path, String markupContent) {
        parse(path, markupContent);

        this.currentIds = new ArrayList<>();
        this.diagramSlides = new DiagramSlides();
        sections.forEach(this::convert);

        return diagramSlides;
    }

    public List<AuxiliaryFile> getAuxiliaryFiles() {
        return parserResult.auxiliaryFiles();
    }

    private void parse(Path path, String markupContent) {
        parserResult = parser.parse(path, markupContent);
        sections = parserResult.docElement().getContent().stream().
                filter(e -> e.getType().equals(DocElementType.SECTION)).collect(toList());
    }

    private void convert(DocElement section) {
        currentIds.add(section.getProp("title").toString());
        if (! section.getContent().isEmpty()) {
            diagramSlides.add(currentIds, section.getContent());
            currentIds = new ArrayList<>();
        }
    }
}
