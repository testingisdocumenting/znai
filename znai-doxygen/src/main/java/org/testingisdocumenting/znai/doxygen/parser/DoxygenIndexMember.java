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

package org.testingisdocumenting.znai.doxygen.parser;

public class DoxygenIndexMember {
    private final DoxygenIndexCompound compound;

    private final String id;
    private final String kind;
    private final String name;

    public DoxygenIndexMember(DoxygenIndexCompound compound, String id, String kind, String name) {
        this.compound = compound;
        this.id = id;
        this.kind = kind;
        this.name = name;
    }

    public DoxygenIndexCompound getCompound() {
        return compound;
    }

    public String getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        if (!compound.getKind().equals("file")) {
            return compound.getName() + "::" + name;
        }

        return name;
    }
}
