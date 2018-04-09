package com.twosigma.documentation.extensions.reactjs

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.code
import static com.twosigma.testing.Ddjt.throwException

class ReactJsComponentIncludePluginTest {
    @Test
    void "should validate custom component name format"() {
        code {
            process("MyComponent")
        } should throwException("component path must be specified as <namespace>.<name>, given: MyComponent")
    }

    @Test
    void "should pass namespace, name and props to CustomReactJSComponent"() {
        def elements = process('myLib.MyComponent {title: "hello"}')
        elements.should == [[type: 'CustomReactJSComponent', namespace: 'myLib',
                             name: 'MyComponent',
                             props:[title: 'hello']]]
    }

    private static List<Map<String, ?>> process(params) {
        return PluginsTestUtils.process(":include-reactjs-component: $params")*.toMap()
    }
}
