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

package org.testingisdocumenting.znai.extensions.api

import org.junit.Test

class ApiLinkedTextTest {
    @Test
    void "should provide combined text"() {
        def linkedText = new ApiLinkedText("hello")
        linkedText.addPart("world", () -> "refid1")
        linkedText.addPart("links ", () -> "refid2")

        linkedText.buildCombinedText().should == "hello world links"
    }
}
