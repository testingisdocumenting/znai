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

package org.testingisdocumenting.znai.python.pydoc;

import org.testingisdocumenting.znai.python.PythonDocParam;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Set;

public class ParsedPythonDoc {
    private static final PythonDocParser DEFAULT = new PythonDocPandasLikeParser();
    private static final Set<PythonDocParser> paramsParsers =
            ServiceLoaderUtils.load(PythonDocParser.class);

    private final String pyDoc;
    private final String pyDocDescriptionOnly;
    private final List<PythonDocParam> params;

    public ParsedPythonDoc(String pyDoc) {
        PythonDocParser parser = findParser();
        PythonDocParserResult parserResult = parser.parse(pyDoc);

        this.pyDoc = pyDoc;
        this.params = parserResult.getParams();
        this.pyDocDescriptionOnly = parserResult.getDescriptionOnly();
    }

    public String getPyDoc() {
        return pyDoc;
    }

    public String getPyDocDescriptionOnly() {
        return pyDocDescriptionOnly;
    }

    public List<PythonDocParam> getParams() {
        return params;
    }

    private PythonDocParser findParser() {
        return paramsParsers.stream()
                .filter(parser -> parser.handles(pyDoc))
                .findFirst().map(PythonDocParser::create)
                .orElse(DEFAULT.create());
    }
}
