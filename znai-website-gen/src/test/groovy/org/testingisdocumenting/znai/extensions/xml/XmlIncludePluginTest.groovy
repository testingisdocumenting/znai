/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions.xml

import org.junit.Test
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class XmlIncludePluginTest {
    @Test
    void "should validate path presence whith multiple children"() {
        code {
            resultingProps("sample.xml", '{paths: ["root.child[4]"]}')
        } should throwException("can't find path: root.child[4] in XML, available:\n" +
                "  root\n" +
                "  root.child[0]\n" +
                "  root.child[1]\n" +
                "  root.child[2]\n" +
                "  root.child[2].@key\n" +
                "  root.child[2].@another\n" +
                "  root.child[2].nested\n" +
                "  root.frost[0]\n" +
                "  root.frost[0].cold")
    }

    @Test
    void "should validate path presence with single path"() {
        code {
            resultingProps("single-path.xml", '{paths: ["root.child[4]"]}')
        } should throwException("can't find path: root.child[4] in XML, available:\n" +
                "  root\n" +
                "  root.frost\n" +
                "  root.frost.cold\n" +
                "  root.frost.cold.@very")
    }

    @Test
    void "should successfully validate path to highlight"() {
        resultingProps("single-path.xml", '{paths: ["root.frost.cold"]}')
    }

    private static Map<String, Object> resultingProps(String fileName, String value) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-xml: $fileName $value")
    }
}
