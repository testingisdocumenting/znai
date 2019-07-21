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

package com.twosigma.znai.groovy.parser

import groovy.transform.Canonical

@Canonical
class GroovyMethod {
    String name
    String nameWithTypes
    String fullBody
    String bodyOnly

    boolean matchesNameAndType(String nameWithOptionalType) {
        def parenthesesOpenIdx = nameWithOptionalType.indexOf('(')
        if (parenthesesOpenIdx == -1) {
            return name == nameWithOptionalType
        }

        def parenthesesCloseIdx = nameWithOptionalType.indexOf(')')
        if (parenthesesCloseIdx == -1) {
            throw new IllegalArgumentException("no closing parentheses: " + nameWithOptionalType)
        }

        def nameOnly = nameWithOptionalType.substring(0, parenthesesOpenIdx)
        def typesAndParentheses = nameWithOptionalType.substring(parenthesesOpenIdx, parenthesesCloseIdx + 1).trim()

        def typesWithNormalizedSpaces = typesAndParentheses.replaceAll(/\s+/, "")

        return nameWithTypes == nameOnly + typesWithNormalizedSpaces
    }
}
