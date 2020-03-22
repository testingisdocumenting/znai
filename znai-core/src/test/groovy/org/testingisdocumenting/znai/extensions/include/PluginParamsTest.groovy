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

package org.testingisdocumenting.znai.extensions.include

import org.testingisdocumenting.znai.extensions.PluginParams
import org.junit.Test

class PluginParamsTest {
    @Test
    void "should parse extra parameters as json"() {
        def params = new PluginParams("test-plugin","free-text {key1: 'value1', key2: 10}")
        assert params.opts.key1 == 'value1'
        assert params.opts.key2 == 10
    }

    @Test
    void "should auto convert rightSide value to meta-rightSide"() {
        def opts = new PluginParams('file', [rightSide: true]).opts.toMap()
        opts.get('rightSide').should == null
        opts.get('meta').should == [rightSide: true]
    }

    @Test
    void "should keep rightSide value as is if plugin is include meta"() {
        def opts = new PluginParams('meta', [rightSide: true]).opts.toMap()
        opts.get('rightSide').should == true
        opts.get('meta').should == null
    }
}
