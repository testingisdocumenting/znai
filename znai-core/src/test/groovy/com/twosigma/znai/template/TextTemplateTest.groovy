package com.twosigma.znai.template

import org.junit.Assert
import org.junit.Test

class TextTemplateTest {
    @Test
    void "processes simple variable placeholders"() {
        def template = new TextTemplate('varPlaceholder','hello ${world}')
        def result = template.process(['world': 'template'])

        result.should == 'hello template'
    }

    @Test
    void "supports loop"() {
        def template = new TextTemplate('list', '<#list names as name>\n${name}\n </#list>')
        def result = template.process(['names': ['A', 'B', 'C']])

        Assert.assertEquals('A\nB\nC\n', result)
    }
}
