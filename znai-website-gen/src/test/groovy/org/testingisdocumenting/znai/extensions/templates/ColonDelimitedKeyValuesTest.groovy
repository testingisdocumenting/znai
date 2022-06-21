/*
 * Copyright 2022 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.extensions.templates

import org.testingisdocumenting.znai.extensions.ColonDelimitedKeyValues
import org.junit.Test

class ColonDelimitedKeyValuesTest {
    @Test
    void "extracts key values from block of text"() {
        def values = new ColonDelimitedKeyValues("""key1:value1
key2:value21
value22
key3: value3
key4:
value41
value42
""")
        values.toMap().should == [key1: "value1", key2: "value21\nvalue22", key3: " value3", key4: "value41\nvalue42"]
    }

    @Test
    void "supports keys that contain non alphabetic symbols"() {
        def values = new ColonDelimitedKeyValues("key@1:value1\nkey@2:value2")
        values.toMap().should == ["key@1": "value1", "key@2": "value2"]
    }

    @Test
    void "supports keys that contain spaces but only when in quotes"() {
        def values = new ColonDelimitedKeyValues("\"key with space\":value1\n" +
                "key 2:value2")
        values.toMap().should == ["key with space": "value1\nkey 2:value2"]
    }

    @Test
    void "key should not start with a space"() {
        def values = new ColonDelimitedKeyValues(" key:value1")
        values.toMap().should == [:]
    }

    @Test
    void "should not confuse link inside value with a column"() {
        def values = new ColonDelimitedKeyValues("key:\n[mylink](https://site)")
        values.toMap().should == [key: "[mylink](https://site)"]
    }
}
