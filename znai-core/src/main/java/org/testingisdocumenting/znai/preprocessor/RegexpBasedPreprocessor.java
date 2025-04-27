/*
 * Copyright 2023 znai maintainers
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

package org.testingisdocumenting.znai.preprocessor;

import org.testingisdocumenting.znai.parser.table.CsvTableParser;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * modifies markup content before parser and extensions take place.
 * the main use-case is to strip/replace html custom blocks in existing markdowns
 */
public class RegexpBasedPreprocessor {
    private final List<RegexpAndReplacement> replacementList;

    public RegexpBasedPreprocessor(List<RegexpAndReplacement> replacementList) {
        this.replacementList = replacementList;
    }

    public RegexpBasedPreprocessor(String csvRegexpReplacementDefinition) {
        MarkupTableData tableData =
                CsvTableParser.parseWithHeader(csvRegexpReplacementDefinition, "regexp", "replacement");

        replacementList = tableData.getData().stream()
                .map(row -> {
                    String replaceWith = row.get(1).toString().replace("\\n", "\n");
                    return new RegexpAndReplacement(row.get(0).toString(), replaceWith);
                })
                .toList();
    }

    public String preprocess(String markdown) {
        for (RegexpAndReplacement regexpAndReplacement : replacementList) {
            Pattern compiled = Pattern.compile(regexpAndReplacement.regexp(), Pattern.DOTALL | Pattern.MULTILINE);
            Matcher matcher = compiled.matcher(markdown);
            if (matcher.find()) {
                markdown = matcher.replaceAll(regexpAndReplacement.replacement());
            }
        }

        return markdown;
    }
}
