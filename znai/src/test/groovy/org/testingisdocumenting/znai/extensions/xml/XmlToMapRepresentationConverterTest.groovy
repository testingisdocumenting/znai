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

package org.testingisdocumenting.znai.extensions.xml

import org.junit.Test

class XmlToMapRepresentationConverterTest {
    @Test
    void "converts children and attributes to their own map entries"() {
        def asMap = XmlToMapRepresentationConverter.convert("""
<ul id="menu" class="test">
   <li id="menu-1" class="item">first</li>
   <li id="menu-2" class="item">second</li>
</ul>
""")

        asMap.should == [
                tagName : 'ul', attributes: [[name: 'id', value: '"menu"'], [name: 'class', value: '"test"']],
                children: [[tagName: 'li', attributes: [[name: 'id', value: '"menu-1"'], [name: 'class', value: '"item"']], children: [
                        [tagName: '', text: 'first']]],
                           [tagName: 'li', attributes: [[name: 'id', value: '"menu-2"'], [name: 'class', value: '"item"']], children: [
                                   [tagName: '', text: 'second']
                           ]]]]
    }
}
