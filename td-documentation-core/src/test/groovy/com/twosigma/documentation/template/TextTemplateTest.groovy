package com.twosigma.documentation.template

import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class TextTemplateTest {
    @Test
    void "processes simple variable placeholders"() {
        def template = new TextTemplate('hello ${world}')
        def result = template.process(['world': 'template'])

        result.should == 'hello template'
    }

    @Test
    void "supports loop"() {
        def template = new TextTemplate('<#list names as name>\n${name}\n </#list>')
        def result = template.process(['names': ['A', 'B', 'C']])

        Assert.assertEquals('A\nB\nC\n', result)
    }
}
