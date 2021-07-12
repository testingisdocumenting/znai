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
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonDocPandasLikeParamsParser implements PythonDocParamsParser {
    private enum LineHandleResult {
        CONTINUE,
        BREAK
    }

    private final String HEADER = "Parameters";

    private final Pattern PARAMETERS_START = Pattern.compile(HEADER + "\\s+_+");

    private final List<PythonParam> params = new ArrayList<>();
    private String currentName = "";
    private String currentType;
    private List<String> currentDocLines;

    @Override
    public boolean handles(String pyDoc) {
        return PARAMETERS_START.matcher(pyDoc).find();
    }

    @Override
    public PythonDocPandasLikeParamsParser create() {
        return new PythonDocPandasLikeParamsParser();
    }

    @Override
    public List<PythonParam> parse(String pyDoc) {
        Matcher matcher = PARAMETERS_START.matcher(pyDoc);
        if (!matcher.find()) {
            throw new RuntimeException("Can't find block with Parameters with underscore");
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
        if (trimmed.equals(HEADER)) {
            return LineHandleResult.CONTINUE;
        }

        if (line.startsWith("__")) {
            return LineHandleResult.CONTINUE;
        }

        if (trimmed.isEmpty() && currentName.isEmpty()) {
            return LineHandleResult.CONTINUE;
        }

        if (line.startsWith(" ") || trimmed.isEmpty()) {
            currentDocLines.add(line);
            return LineHandleResult.CONTINUE;
        }

        String[] parts = trimmed.split(":");
        if (parts.length == 2) {
            startNewParam(parts[0], parts[1]);
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

        params.add(new PythonParam(currentName.trim(),
                currentType.trim(),
                StringUtils.stripIndentation(String.join("\n", currentDocLines))));
        currentName = "";
    }
}
