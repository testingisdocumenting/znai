package com.twosigma.znai.jupyter

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class JupyterIncludePluginTest {
    @Test
    void "should split each cell and create separate doc elements for each input and output"() {
        def elements = process("jupyter-notebook.ipynb")
        elements.should == [[type: 'JupyterCell', cellType: 'code', snippet: 'from pandas import read_csv\nfrom IPython.display import display', lang: 'python'],
                            [type: 'JupyterCell', cellType: 'empty-output', meta: [rightSide: true]],
                            [type: 'JupyterCell', cellType: 'code', snippet: "tran = read_csv('transport.csv')\nprint(tran)", lang: 'python'],
                            [type: 'JupyterCell', cellType: 'output', text: '   a   b   c\n' +
                                    '0  1   2   3\n' +
                                    '1  4   5   6\n', meta: [rightSide: true]],
                            [type: 'JupyterCell', cellType: 'code', snippet: 'display(tran)', lang: 'python'],
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
                             'JupyterCell'  | 'code'         | true

                             'TestMarkdown' | ''             | false
                             'JupyterCell'  | 'empty-output' | true

                             'JupyterCell'  | 'output'       | false
                             'JupyterCell'  | 'code'         | true

                             'TestMarkdown' | ''             | false
                             'JupyterCell'  | 'empty-output' | true

                             'JupyterCell'  | 'output'       | false
                             'JupyterCell'  | 'code'         | true  }

    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-jupyter: $params")
        return result*.toMap()
    }
}
