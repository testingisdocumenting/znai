package com.twosigma.znai.extensions.api

import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.Test

import static com.twosigma.webtau.Ddjt.equal

class ApiParametersCsvParserTest {
    @Test
    void "should convert dot separated names to api parameter with nested children"() {
        def apiParameters = ApiParametersCsvParser.parse(TestComponentsRegistry.INSTANCE.markdownParser, """
firstName, String, descr1
nested, object, descr2
nested.zipCode, String, descr3
nested.address, String, descr4
nested.subNested, object, nested nested
nested.subNested.url, String, nested nested 1
nested.subNested.fileName, String, nested nested 2
nestedList, array of objects, descr5
nestedList.score, int, descr6
""")

        apiParameters.toMap().should equal([parameters: [
                [name: 'firstName', type: 'String', description: [[markdown: 'descr1', type: 'TestMarkdown']]],
                [name: 'nested', type: 'object', description: [[markdown: 'descr2', type: 'TestMarkdown']], children:
                        [[name: 'zipCode', type: 'String', description: [[markdown: 'descr3', type: 'TestMarkdown']]],
                         [name: 'address', type: 'String', description: [[markdown: 'descr4', type: 'TestMarkdown']]],
                         [name: 'subNested', type: 'object', description: [[markdown: 'nested nested', type: 'TestMarkdown']], children:
                                 [[name: 'url', type: 'String', description: [[markdown: 'nested nested 1', type: 'TestMarkdown']]],
                                  [name: 'fileName', type: 'String', description: [[markdown: 'nested nested 2', type: 'TestMarkdown']]]]]]],
                [name: 'nestedList', type: 'array of objects', description: [[markdown: 'descr5', type: 'TestMarkdown']], children: [
                        [name: 'score', type: 'int', description: [[markdown: 'descr6', type: 'TestMarkdown']]]]]]])
    }
}
