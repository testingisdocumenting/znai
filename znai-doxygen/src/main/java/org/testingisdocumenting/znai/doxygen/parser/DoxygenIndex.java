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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoxygenIndex {
    private final Map<String, DoxygenIndexCompound> compoundById;
    private final Map<String, DoxygenIndexMember> memberById;

    public DoxygenIndex() {
        this.compoundById = new LinkedHashMap<>();
        this.memberById = new LinkedHashMap<>();
    }

    public void addCompound(DoxygenIndexCompound compound) {
        compoundById.put(compound.getId(), compound);
    }

    public void addMember(DoxygenIndexMember member) {
        memberById.put(member.getId(), member);
    }

    public Map<String, DoxygenIndexCompound> getCompoundById() {
        return compoundById;
    }

    public Map<String, DoxygenIndexMember> getMemberById() {
        return memberById;
    }

    public DoxygenIndexCompound findCompoundByName(String fullName) {
        return compoundById.values().stream()
                .filter((c) -> fullName.equals(c.getName()))
                .findFirst()
                .orElse(null);
    }

    public DoxygenIndexMember findMemberByName(String fullName) {
        return memberById.values().stream()
                .filter((m) -> fullName.equals(m.getFullName()))
                .findFirst()
                .orElse(null);
    }

    public String renderAvailableNames() {
        return Stream.concat(
                        compoundById.values().stream()
                                .filter(c -> !c.getKind().equals("file"))
                                .map(DoxygenIndexCompound::getName),
                        memberById.values().stream().map(DoxygenIndexMember::getFullName))
                .sorted()
                .distinct()
                .collect(Collectors.joining("\n"));
    }
}
