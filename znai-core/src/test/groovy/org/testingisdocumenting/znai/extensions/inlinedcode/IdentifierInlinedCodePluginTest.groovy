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

package org.testingisdocumenting.znai.extensions.inlinedcode

import org.junit.Test
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class IdentifierInlinedCodePluginTest {
    @Test
    void "validates identifier"() {
        resultingProps("myFunc", "validationCode.ext")
    }

    @Test
    void "identifier should be a full word match"() {
        code {
            resultingProps("myFu", "validationCode.ext")
        } should throwException("can't find <myFu> identifier in: validationCode.ext")
    }

    private static Map<String, Object> resultingProps(String identifier, String fileName) {
        return PluginsTestUtils.processInlinedCodeAndGetProps(
                ":identifier: $identifier {validationPath: '$fileName'}")
    }
}
