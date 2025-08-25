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

package org.testingisdocumenting.znai.jupyter

import org.junit.Test

class JupyterCellFilterTest {
    @Test
    void "should return empty list when section is not found"() {
        def cells = [
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Introduction", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "print('hello')", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Conclusion", [])
        ]
        
        def result = JupyterCellFilter.fromSection(cells, "NonExistent")
        
        result.size().should == 0
    }
    
    @Test
    void "should return all cells from section to end when no next section"() {
        def cells = [
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Introduction", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "x = 1", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Last Section", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "print('in last')", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "Some text at the end", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "final_code()", [])
        ]
        
        def result = JupyterCellFilter.fromSection(cells, "Last Section")
        
        result.size().should == 4
        result[0].input.should == "# Last Section"
        result[1].input.should == "print('in last')"
        result[2].input.should == "Some text at the end"
        result[3].input.should == "final_code()"
    }
    
    @Test
    void "should handle sections with whitespace around header"() {
        def cells = [
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "  # Section One  ", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "code1()", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "\n# Section Two\n", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "code2()", [])
        ]
        
        def result = JupyterCellFilter.fromSection(cells, "Section One")
        
        result.size().should == 2
        result[0].input.should == "  # Section One  "
        result[1].input.should == "code1()"
    }
    
    @Test
    void "should find section with any header level"() {
        def cells = [
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Top Level", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "code1()", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "## Second Level Section", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "code2()", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "### Third Level", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "code3()", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "#### Fourth Level Section", []),
            new JupyterCell(JupyterCell.CODE_TYPE, "code4()", []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Another Top", [])
        ]
        
        def result2 = JupyterCellFilter.fromSection(cells, "Second Level Section")
        result2.size().should == 2
        result2[0].input.should == "## Second Level Section"
        result2[1].input.should == "code2()"
        
        def result4 = JupyterCellFilter.fromSection(cells, "Fourth Level Section")
        result4.size().should == 2
        result4[0].input.should == "#### Fourth Level Section"
        result4[1].input.should == "code4()"
    }
    
    @Test
    void "should handle cells with null input"() {
        def cells = [
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Section", []),
            new JupyterCell(JupyterCell.CODE_TYPE, null, []),
            new JupyterCell(JupyterCell.MARKDOWN_TYPE, "# Next", [])
        ]
        
        def result = JupyterCellFilter.fromSection(cells, "Section")
        
        result.size().should == 2
        result[0].input.should == "# Section"
        result[1].input.should == null
    }
}