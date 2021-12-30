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

package org.testingisdocumenting.znai.doxygen.plugin;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.doxygen.Doxygen;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMember;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMembersList;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;

import java.util.stream.Stream;

class DoxygenMemberListExtractor {
    static final String INCLUDE_ALL_MATCHES_KEY = "includeAllMatches";
    static final String ARGS_KEY = "args";

    static DoxygenMembersList extract(Doxygen doxygen, ComponentsRegistry componentsRegistry,
                                      PluginParamsOpts opts, boolean allowAll, String fullName) {
        if (allowAll && opts.has(INCLUDE_ALL_MATCHES_KEY) && opts.has(ARGS_KEY)) {
            throw new IllegalArgumentException("can't specify " + INCLUDE_ALL_MATCHES_KEY + " and " + ARGS_KEY +
                    " at the same time");
        }

        boolean includeAllMatches = allowAll && opts.get(INCLUDE_ALL_MATCHES_KEY, false);
        String argsToFilter = opts.get(ARGS_KEY, "");

        DoxygenMembersList membersList;

        if (includeAllMatches) {
            membersList = doxygen.findAndParseAllMembers(componentsRegistry, fullName);
        } else if (!argsToFilter.isEmpty()) {
            membersList = doxygen.findAndParseAllMembers(componentsRegistry, fullName);
            DoxygenMember memberByArgs = membersList.findByArgs(argsToFilter);
            if (memberByArgs == null) {
                throw new RuntimeException("can't find member " + fullName + " with args: <" + argsToFilter + "> " +
                        "available args:\n" + membersList.renderAvailableArgs());
            }

            membersList = new DoxygenMembersList(Stream.of(memberByArgs));
        } else {
            membersList = new DoxygenMembersList(
                    Stream.of(doxygen.getCachedOrFindAndParseMember(componentsRegistry, fullName)));
        }

        return membersList;
    }

    static void throwIfMembersListIsEmpty(Doxygen doxygen, ComponentsRegistry componentsRegistry, String fullName) {
        throw new RuntimeException("can't find member: " + fullName + ", available names:\n" +
                doxygen.buildIndexOrGetCached(componentsRegistry).renderAvailableMemberNames());
    }
}
