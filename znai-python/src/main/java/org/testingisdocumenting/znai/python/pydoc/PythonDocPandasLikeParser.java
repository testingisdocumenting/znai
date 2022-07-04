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
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonDocPandasLikeParser implements PythonDocParser {
    private enum LineHandleResult {
        CONTINUE,
        BREAK
    }

    private final String PARAMETERS_HEADER = "Parameters";
    private final String UNDERSCORE_PATTERN = "\\s+[-_]+";

    private final Pattern HEADER_START = Pattern.compile("\\w+" + UNDERSCORE_PATTERN);
    private final Pattern PARAMETERS_START = Pattern.compile(PARAMETERS_HEADER + UNDERSCORE_PATTERN);
//    private final Pattern PARAMETER_NAME_TYPE = Pattern.compile("^(\\w+)\\s*:\\s*(.*)\\s*");
    private final Pattern PARAMETER_NAME_TYPE = Pattern.compile("^(\\S+)\\s*:\\s*(.*)\\s*");

    private final List<PythonDocParam> params = new ArrayList<>();
    private String currentName = "";
    private String currentType;
    private List<String> currentDocLines;

    @Override
    public boolean handles(String pyDoc) {
        if (pyDoc == null) {
            return false;
        }

        return HEADER_START.matcher(pyDoc).find();
    }

    @Override
    public PythonDocPandasLikeParser create() {
        return new PythonDocPandasLikeParser();
    }

    @Override
    public PythonDocParserResult parse(String pyDoc) {
        String descriptionOnly = extractDescriptionOnly(pyDoc);
        List<PythonDocParam> params = parseParams(pyDoc);

        return new PythonDocParserResult(descriptionOnly, params);
    }

    private String extractDescriptionOnly(String pyDoc) {
        Matcher matcher = HEADER_START.matcher(pyDoc);
        if (!matcher.find()) {
            return pyDoc;
        }

        return pyDoc.substring(0, matcher.start()).trim();
    }

    private List<PythonDocParam> parseParams(String pyDoc) {
        Matcher matcher = PARAMETERS_START.matcher(pyDoc);
        if (!matcher.find()) {
            return Collections.emptyList();
        }
        int start = matcher.start();
        String fromParams = pyDoc.substring(start);

        String[] lines = fromParams.split("\n");
        for (String line : lines) {
            LineHandleResult result = handleLine(line);
            if (result == LineHandleResult.BREAK) {
                break;
            }
        }

        flushParamIfRequired();

        return Collections.unmodifiableList(params);
    }

    private LineHandleResult handleLine(String line) {
        String trimmed = line.trim();
        if (trimmed.equals(PARAMETERS_HEADER)) {
            return LineHandleResult.CONTINUE;
        }

        if (line.startsWith("___") || line.startsWith("---")) {
            return LineHandleResult.CONTINUE;
        }

        if (trimmed.isEmpty() && currentName.isEmpty()) {
            return LineHandleResult.CONTINUE;
        }

        if (line.startsWith(" ") || trimmed.isEmpty()) {
            if (currentDocLines == null) {
                throw new IllegalStateException("unexpected parameter description found: define parameter first");
            }

            currentDocLines.add(line);
            return LineHandleResult.CONTINUE;
        }

        Matcher parameterNameTypeMatcher = PARAMETER_NAME_TYPE.matcher(trimmed);
        if (parameterNameTypeMatcher.find()) {
            startNewParam(parameterNameTypeMatcher.group(1), parameterNameTypeMatcher.group(2));
            return LineHandleResult.CONTINUE;
        }

        return LineHandleResult.BREAK;
    }

    private void startNewParam(String name, String type) {
        flushParamIfRequired();
        currentName = name;
        currentType = type;
        currentDocLines = new ArrayList<>();
    }

    private void flushParamIfRequired() {
        if (currentName.isEmpty()) {
            return;
        }

        params.add(new PythonDocParam(currentName.trim(),
                currentType.trim(),
                StringUtils.stripIndentation(String.join("\n", currentDocLines))));
        currentName = "";
    }
}
