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

package com.twosigma.znai.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.twosigma.znai.utils.StringUtils;

import java.util.List;

public class JavaCodeUtils {
    public static String removeSemicolonAtEnd(String code) {
        return code.endsWith(";") ? code.substring(0, code.length() - 1) : code;
    }

    public static String removeReturn(String code) {
        return code.replace("return", "      ");
    }

    static String extractCode(List<String> lines, BodyDeclaration declaration) {
        Range range = declaration.getRange().orElseGet(() -> new Range(new Position(0, 0), new Position(0, 0)));
        int startLine = range.begin.line - 1;
        int endLine = range.end.line - 1;

        return String.join("\n", lines.subList(startLine, endLine + 1));
    }

    static String extractSignature(String code) {
        int i = code.indexOf('{');
        return StringUtils.stripIndentation(i == -1 ? code : code.substring(0, i)).trim();
    }
}
