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

import org.junit.Test

class OutputLinesCompareToHandlerTest {
    @Test
    void "should mark lines that passed assertion"() {
        def lines = new OutputLines("out", "line1\nline2\nline3\nline4")
        lines.should == 'line2'
        lines.should == 'line4'

        lines.getCheckedLineIdx().should == [1, 3]
    }

    @Test
    void "should handle not equals"() {
        def lines = new OutputLines("out", "line1\nline2\nline3\nline4")
        lines.shouldNot == 'foo'
    }
}
