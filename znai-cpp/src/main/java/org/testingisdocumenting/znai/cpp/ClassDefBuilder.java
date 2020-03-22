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

package org.testingisdocumenting.znai.cpp;

import org.testingisdocumenting.znai.cpp.parser.EntryDef;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ClassDefBuilder {
    private String name;
    private int startLine;
    private int endLine;
    private StringBuilder bodyOnly;
    private int nonClosedScopes;
    private List<String> codeLines;

    public ClassDefBuilder(List<String> codeLines) {
        this.codeLines = codeLines;
        bodyOnly = new StringBuilder();
    }

    public void addToBody(String text) {
        bodyOnly.append(text);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public EntryDef build() {
        return new EntryDef(name,
                codeLines.subList(startLine - 1, endLine).stream().collect(Collectors.joining("\n")),
                stripIndentation());
    }

    private String stripIndentation() {
        return StringUtils.stripIndentation(bodyOnly.substring(1, bodyOnly.length() - 1));
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", startLine=" + startLine +
                ", endLine=" + endLine +
                ", bodyOnly=" + bodyOnly;
    }

    public void scopeOpen() {
        nonClosedScopes++;
    }

    public void scopeClose() {
        nonClosedScopes--;
    }

    public boolean isMainScopeClosed() {
        return nonClosedScopes == 0;
    }
}
