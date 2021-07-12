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

import org.testingisdocumenting.znai.python.PythonParam;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Set;

public class ParsedPythonDoc {
    private static final Set<PythonDocParamsParser> paramsParsers =
            ServiceLoaderUtils.load(PythonDocParamsParser.class);

    private final String pyDoc;
    private final List<PythonParam> params;

    public ParsedPythonDoc(String pyDoc) {
        this.pyDoc = pyDoc;
        this.params = createParamsParser().parse(pyDoc);
    }

    public String getPyDoc() {
        return pyDoc;
    }

    public List<PythonParam> getParams() {
        return params;
    }

    private PythonDocParamsParser createParamsParser() {
        return paramsParsers.stream()
                .filter(parser -> parser.handles(pyDoc))
                .findFirst().map(PythonDocParamsParser::create)
                .orElseThrow(() -> new RuntimeException(
                        "can't find pythod doc params parser to parse:\n" + pyDoc));
    }
}
