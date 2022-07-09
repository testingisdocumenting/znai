/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.python

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PropsUtils

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class PythonCodeTypeTest {
    def docStructure = TEST_COMPONENTS_REGISTRY.docStructure()

    @Test
    void "render as string"() {
        def type = new PythonCodeType([name: "Union", types: [
                [name: "Context", types: []],
                [name: "list", types: [[name: "Context", types: []]]],
                [name: "string", types: []],
        ]], "")

        type.renderTypeAsString().should == "Union[Context, list[Context], string]"
    }

    @Test
    void "render simple as linked text"() {
        def type = new PythonCodeType([name: "finance.Money", types: []], "")

        docStructure.fakeGlobalAnchors = [
                "python_api_finance_Money": "/api/money",
        ]

        PropsUtils.exerciseSuppliers(type.convertToApiLinkedText(docStructure).toListOfMaps()).should == [
                [text: "finance.Money", url: "/api/money"]]
    }

    @Test
    void "render empty as linked text"() {
        def type = new PythonCodeType([name: "", types: []], "")
        PropsUtils.exerciseSuppliers(type.convertToApiLinkedText(docStructure).toListOfMaps()).should == []
    }

    @Test
    void "render complex as linked text"() {
        def type = new PythonCodeType([name: "Union", types: [
                [name: "finance.Money", types: []],
                [name: "list", types: [[name: "finance.Dept", types: []]]],
                [name: "string", types: []],
        ]], "")

        def docStructure = TEST_COMPONENTS_REGISTRY.docStructure()
        docStructure.fakeGlobalAnchors = [
                "python_api_finance_Money": "/api/money",
                "python_api_finance_Dept": "/api/dept"
        ]

        PropsUtils.exerciseSuppliers(type.convertToApiLinkedText(docStructure).toListOfMaps()).should == [
                [text: "Union", url: ""],
                [text: "[", url: ""],
                [text: "finance.Money", url: "/api/money"],
                [text: ", ", url: ""],
                [text: "list", url: ""],
                [text: "[", url: ""],
                [text: "finance.Dept", url: "/api/dept"],
                [text: "]", url: ""],
                [text: ", ", url: ""],
                [text: "string", url: ""],
                [text: "]", url: ""]]
    }
}
