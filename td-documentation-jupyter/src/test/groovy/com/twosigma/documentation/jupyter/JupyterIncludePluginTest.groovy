package com.twosigma.documentation.jupyter

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

class JupyterIncludePluginTest {
    @Test
    void "should split each cell and create separate doc elements for each input and output"() {
        def elements = process("jupyter-notebook.json")
        elements.should == [[type: 'JupyterCell', sourceTokens: [[type: 'text', content: 'from pandas import read_csv\nfrom IPython.display import display']]],
                            [type: 'JupyterCell', sourceTokens: [[type: 'text', content: "tran = read_csv('transport.csv')\nprint(tran)"]]],
                            [type: 'JupyterCell', text: '   a   b   c\n' +
                                    '0  1   2   3\n' +
                                    '1  4   5   6\n'],
                            [type: 'JupyterCell', sourceTokens: [[type: 'text', content: 'display(tran)']]],
                            [type: 'JupyterCell', html:'<table border="1" class="dataframe">\n' +
                                    '  <thead>\n' +
                                    '  </thead>\n' +
                                    '</table>\n'],
                            [type: 'TestMarkdown', markdown: '# this is great\n\nlike good **old** times']]
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-jupyter: $params")
        return result*.toMap()
    }
}
