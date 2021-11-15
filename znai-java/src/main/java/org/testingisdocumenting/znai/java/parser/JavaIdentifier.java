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

package org.testingisdocumenting.znai.java.parser;

import java.util.List;
import java.util.stream.Collectors;

public class JavaIdentifier {
    private final List<String> parentNames;
    private final String name;
    private final String typesInParentheses;

    public JavaIdentifier(List<String> parentNames, String name, String typesInParentheses) {
        this.parentNames = parentNames;
        this.name = name;
        this.typesInParentheses = typesInParentheses;
    }

    public boolean matches(String fullOrPartialName) {
        int openParenthesisIdx = fullOrPartialName.indexOf('(');
        boolean hasTypes = openParenthesisIdx != -1;
        String givenTypes = hasTypes ? fullOrPartialName.substring(openParenthesisIdx) : "";
        String giveNameWithoutTypes = hasTypes ? fullOrPartialName.substring(0, openParenthesisIdx) : fullOrPartialName;

        String[] parts = giveNameWithoutTypes.split("\\.");
        boolean matchName = name.equals(parts[parts.length - 1]) &&
                (!hasTypes || typesInParentheses.equals(givenTypes));

        return matchName && matchesPrefix(parts);
    }

    public String getName() {
        return name + typesInParentheses;
    }

    public String getFullName() {
        return parentNames.isEmpty() ? getName() :
                String.join(".", parentNames) + "." + getName();
    }

    public String getFullNameWithoutFirstParent() {
        return parentNames.size() < 2 ? getName() :
                parentNames.stream().skip(1).collect(Collectors.joining(".")) + "." + getName();
    }

    @Override
    public String toString() {
        return "JavaIdentifier{" +
                "parentNames=" + parentNames +
                ", name='" + name + '\'' +
                ", typesInParentheses='" + typesInParentheses + '\'' +
                '}';
    }

    private boolean matchesPrefix(String[] partsIncludingName) {
        int inputPrefixIdx = partsIncludingName.length - 2; // skipping name
        int parentIdx = parentNames.size() - 1;

        while (inputPrefixIdx >= 0 && parentIdx >= 0) {
            if (!parentNames.get(parentIdx).equals(partsIncludingName[inputPrefixIdx])) {
                return false;
            }

            inputPrefixIdx--;
            parentIdx--;
        }

        return true;
    }
}
