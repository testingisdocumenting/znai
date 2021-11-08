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

import java.util.LinkedHashMap;
import java.util.Map;

public class DoxygenIndex {
    private final Map<String, DoxygenIndexMember> memberById;

    public DoxygenIndex() {
        this.memberById = new LinkedHashMap<>();
    }

    public void add(DoxygenIndexMember member) {
        memberById.put(member.getId(), member);
    }

    public Map<String, DoxygenIndexMember> getMemberById() {
        return memberById;
    }

    public DoxygenIndexMember findByName(String nameOrFullName) {
        return memberById.values().stream()
                .filter((m) -> matches(m, nameOrFullName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can't find entry with name <" + nameOrFullName + ">"));
    }

    private boolean matches(DoxygenIndexMember doxygenIndexMember, String nameOrFullName) {
        return nameOrFullName.equals(doxygenIndexMember.getName()) ||
                nameOrFullName.equals(doxygenIndexMember.getFullName());
    }
}
