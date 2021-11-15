/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.java.parser;

import java.util.List;

public class JavaField {
    private final JavaIdentifier identifier;
    private final String name;
    private final String javaDocText;

    public JavaField(List<String> parentNames, String name, String javaDocText) {
        this.identifier = new JavaIdentifier(parentNames, name, "");
        this.name = name;
        this.javaDocText = javaDocText;
    }

    public String getFullNameWithoutFirstParent() {
        return identifier.getFullNameWithoutFirstParent();
    }

    public String getName() {
        return name;
    }

    public String getJavaDocText() {
        return javaDocText;
    }

    public boolean matches(String fullOrPartialName) {
        return identifier.matches(fullOrPartialName);
    }

    @Override
    public String toString() {
        return "JavaField{" +
                "identifier='" + identifier + '\'' +
                ", javaDocText='" + javaDocText + '\'' +
                '}';
    }
}
