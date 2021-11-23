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
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DoxygenCompound {
    private final Map<String, DoxygenMember> memberById;

    private String id;
    private String kind;
    private String name;
    private DoxygenDescription description;

    public DoxygenCompound() {
        memberById = new LinkedHashMap<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(DoxygenDescription description) {
        this.description = description;
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

    public DoxygenDescription getDescription() {
        return description;
    }

    public void addMember(DoxygenMember member) {
        memberById.put(member.getId(), member);
    }

    public DoxygenMember findById(String id) {
        return memberById.get(id);
    }

    public DoxygenMember findByFullName(String fullName) {
        return membersStream().filter(m -> fullName.equals(m.getFullName()))
                .findFirst()
                .orElse(null);
    }

    public Stream<DoxygenMember> membersStream() {
        return memberById.values().stream();
    }

    public Stream<DoxygenMember> publicNonStaticFunctionsStream() {
        return publicFunctionsStream().filter(member -> !member.isStatic());
    }

    public Stream<DoxygenMember> protectedFunctionsStream() {
        return filterStream(DoxygenMember::isProtected);
    }

    public Stream<DoxygenMember> publicStaticFunctionsStream() {
        return publicFunctionsStream().filter(DoxygenMember::isStatic);
    }

    public Stream<DoxygenMember> publicNonStaticAttributesStream() {
        return publicAttributesStream().filter(member -> !member.isStatic());
    }

    public Stream<DoxygenMember> publicStaticAttributesStream() {
        return publicAttributesStream().filter(DoxygenMember::isStatic);
    }

    public Stream<DoxygenMember> publicAttributesStream() {
        return filterStream(member -> member.isVariable() && member.isPublic());
    }

    public Stream<DoxygenMember> publicFunctionsStream() {
        return filterStream(member -> member.isFunction() && member.isPublic());
    }

    private Stream<DoxygenMember> filterStream(Predicate<DoxygenMember> predicate) {
        return memberById.values().stream().filter(predicate);
    }
}
