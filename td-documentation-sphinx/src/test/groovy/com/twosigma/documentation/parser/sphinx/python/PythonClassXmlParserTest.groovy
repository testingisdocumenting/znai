package com.twosigma.documentation.parser.sphinx.python

import com.twosigma.utils.ResourceUtils
import org.junit.BeforeClass
import org.junit.Test

/**
 * @author mykola
 */
class PythonClassXmlParserTest {
    static def xml = ResourceUtils.textContent('auto-class.xml')
    static PythonClass pythonClass

    @BeforeClass
    static void init() {
        def parser = new PythonClassXmlParser()
        pythonClass = parser.parse(xml)
    }

    @Test
    void "parse class name and description"() {
        pythonClass.should == [name: 'HelloWorld', description: 'Simple hello world class']
    }

    @Test
    void "parse method name, description and params"() {
        def params = [ 'name' | 'description'] {
                      _______________________________________
                       'name' | 'name of a person to greet'
                      'title' | 'title of a person to greet' }

        pythonClass.methods.should == [  'name' | 'description' | 'params'] {
                                      ______________________________________
                                       'sayBye' |    'says bye' | params
                                       'sayHi'  |    'says hi'  | params }
    }
}
