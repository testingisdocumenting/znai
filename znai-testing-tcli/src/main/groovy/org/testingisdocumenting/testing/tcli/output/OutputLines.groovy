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

package com.twosigma.testing.tcli.output

import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.ActualPathAware

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
