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

package com.twosigma.znai.jupyter

import com.twosigma.utils.JsonUtils
import com.twosigma.utils.ResourceUtils
import org.junit.BeforeClass
import org.junit.Test

class JupyterParserVer4Test {
    static JupyterNotebook notebook

    @BeforeClass
    static void init() {
        JupyterParser parser = new JupyterParserVer4()
        notebook = parser.parse(JsonUtils.deserializeAsMap(
                ResourceUtils.textContent("jupyter-notebook.ipynb")))
    }

    @Test
    void "parse lang"() {
        notebook.lang.should == 'python'
    }

    @Test
    void "parse markdown cells"() {
        def markdownCell = notebook.cells.find { it.type == 'markdown' }
        markdownCell.type.should == 'markdown'
        markdownCell.input.should == '# this is great\n\nlike good **old** times'
        markdownCell.outputs.should == []
    }

    @Test
    void "parse code cells"() {
        notebook.cells.findAll { it.type == 'code' }.size().should == 3
    }

    @Test
    void "should detect console output"() {
        def output = notebook.cells[1].outputs[0]
        output.format.should == 'text'
        output.content.should == '   a   b   c\n' +
                '0  1   2   3\n' +
                '1  4   5   6\n'
    }

    @Test
    void "should extract html from data output"() {
        def htmlOutput = notebook.cells[2].outputs[0]
        htmlOutput.format.should == 'html'
        htmlOutput.content.should == '<table border="1" class="dataframe">\n' +
                '  <thead>\n' +
                '  </thead>\n' +
                '</table>\n'
    }
}
