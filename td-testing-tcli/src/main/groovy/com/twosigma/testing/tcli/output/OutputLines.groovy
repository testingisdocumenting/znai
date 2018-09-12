package com.twosigma.testing.tcli.output

import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.ActualPathAware

/**
 * @author mykola
 */
class OutputLines implements ActualPathAware {
    private List<String> lines
    private Set<Integer> checkedLineIdx
    private String output
    private ActualPath actualPath

    OutputLines(String id, String output) {
        this.actualPath = new ActualPath(id)
        this.output = output
        this.lines = output.replaceAll('\r', '').split('\n')
        this.checkedLineIdx = [] as TreeSet
    }

    void registerCheckedLine(int idx) {
        checkedLineIdx.add(idx)
    }

    Set<Integer> getCheckedLineIdx() {
        return checkedLineIdx
    }

    List<String> getLines() {
        return lines
    }

    @Override
    String toString() {
        return output
    }

    @Override
    ActualPath actualPath() {
        return actualPath
    }
}
