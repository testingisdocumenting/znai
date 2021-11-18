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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoxygenMembersList {
    private final List<DoxygenMember> members;

    public DoxygenMembersList() {
        members = new ArrayList<>();
    }

    public DoxygenMembersList(Stream<DoxygenMember> memberStream) {
        members = memberStream.collect(Collectors.toList());
    }

    public void add(DoxygenMember member) {
        members.add(member);
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public void forEach(Consumer<DoxygenMember> consumer) {
        members.forEach(consumer);
    }

    public DoxygenMember first() {
        return members.get(0);
    }

    public DoxygenMember findByArgs(String args) {
        return members.stream()
                .filter(member -> member.matchesArgs(args))
                .findFirst()
                .orElse(null);
    }

    public String renderAvailableArgs() {
        return members.stream().map(DoxygenMember::getArgs).collect(Collectors.joining("\n"));
    }
}
