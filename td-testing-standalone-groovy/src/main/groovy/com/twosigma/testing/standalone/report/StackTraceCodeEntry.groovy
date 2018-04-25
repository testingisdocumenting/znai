package com.twosigma.testing.standalone.report

import groovy.transform.Canonical

@Canonical
class StackTraceCodeEntry {
    String filePath
    int lineNumber
}
