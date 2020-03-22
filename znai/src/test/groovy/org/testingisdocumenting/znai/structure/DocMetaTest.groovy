/*
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

package com.twosigma.znai.structure

import org.junit.Test

class DocMetaTest {
    @Test
    void "should create a new doc meta instance based on existing data and new json"() {
        def existing = new DocMeta([title: 'old title', type: 'old type'])
        existing.id = 'doc-id'
        existing.previewEnabled = true

        def clone = existing.cloneWithNewJson("""
{"title": "new title", 
"type": "guide"}
""")

        clone.id.should == 'doc-id'
        clone.previewEnabled.should == true
        clone.title.should == 'new title'
        clone.type.should == 'guide'
    }
}
