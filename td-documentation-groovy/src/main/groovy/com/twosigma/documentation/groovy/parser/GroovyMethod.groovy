package com.twosigma.documentation.groovy.parser

import groovy.transform.Canonical

/**
 * @author mykola
 */
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
