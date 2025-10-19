/*
 * Copyright 2020 znai maintainers
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

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class JupyterIncludePluginTest {
    @Test
    void "should split each cell and create separate doc elements for each input and output"() {
        def elements = process("jupyter-notebook.ipynb")
        elements.should == [[type: 'Snippet', snippet: 'from pandas import read_csv\nfrom IPython.display import display', lang: 'python', className: "znai-jupyter-cell", lineNumber: ""],
                            [type: 'JupyterCell', cellType: 'empty-output', meta: [rightSide: true]],
                            [type: 'Snippet',  snippet: "tran = read_csv('transport.csv')\nprint(tran)", lang: 'python', className: "znai-jupyter-cell", lineNumber: "", noGap: true, noGapBorder: true],
                            [type: 'Snippet', snippet: '   a   b   c\n' +
                                    '0  1   2   3\n' +
                                    '1  4   5   6', meta: [rightSide: true], className: "znai-jupyter-cell", lineNumber: "", lang: "csv", resultOutput: true],
                            [type: 'Snippet', snippet: 'display(tran)', lang: 'python', noGap: true, className: "znai-jupyter-cell", lineNumber: ""],
                            [type: 'JupyterCell', cellType: 'output', meta: [rightSide: true], html:'<table border="1" class="dataframe">\n' +
                                    '  <thead>\n' +
                                    '  </thead>\n' +
                                    '</table>\n'],
                            [type: 'TestMarkdown', markdown: '# this is great\n\nlike good **old** times'],
                            [type: 'JupyterCell', cellType: 'empty-output', meta: [rightSide: true]]]
    }

    @Test
    void "for storyFirst mode it should swap input and output, but leave markdown cells in place"() {
        def elements = process("notebook-with-markdown-story.ipynb {storyFirst: true}")

        def cellTypes = elements.collect { [
                type: it.type,
                cellType: it.cellType ?: '',
                rightSide: it.meta?.rightSide ?: false] }

        cellTypes.should == [       'type'  | 'cellType'     | 'rightSide'] {
                             _________________________________________________
                             'TestMarkdown' | ''             | false
                             'JupyterCell'  | 'empty-output' | true

                             'JupyterCell'  | 'empty-output' | true
                             'Snippet'      | ''             | true

                             'TestMarkdown' | ''             | false
                             'JupyterCell'  | 'empty-output' | true

                             'Snippet'      | ''             | false
                             'Snippet'      | ''             | true

                             'TestMarkdown' | ''             | false
                             'JupyterCell'  | 'empty-output' | true

                             'JupyterCell'  | 'output'       | false
                             'Snippet'      | ''             | true  }

    }

    private static def process(String params) {
        def result = PluginsTestUtils.processInclude(":include-jupyter: $params")
        return result*.toMap()
    }
}
