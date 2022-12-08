/*
 * Copyright 2021 znai maintainers
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

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParamsFactory

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class PluginParamsTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()
    
    @Test
    void "should parse extra parameters as json"() {
        def params = pluginParamsFactory.create("test-plugin", 'free-text {key1: "value1", key2: 10}')
        assert params.opts.key1 == "value1"
        assert params.opts.key2 == 10
    }

    @Test
    void "should auto convert rightSide value to meta-rightSide"() {
        def opts = pluginParamsFactory.create("file", "", [rightSide: true]).opts.toMap()
        opts.get("rightSide").should == null
        opts.get("meta").should == [rightSide: true]
    }

    @Test
    void "should auto convert stickySlide value to meta-stickySlide"() {
        def opts = pluginParamsFactory.create("file", "", [stickySlide: "top"]).opts.toMap()
        opts.get("stickySlide").should == null
        opts.get("meta").should == [stickySlide: "top"]
    }

    @Test
    void "should keep rightSide value as is if plugin is include meta"() {
        def opts = pluginParamsFactory.create("meta", "", [rightSide: true]).opts.toMap()
        opts.get("rightSide").should == true
        opts.get("meta").should == null
    }

    @Test
    void "should keep stickySlide value as is if plugin is include meta"() {
        def opts = pluginParamsFactory.create("meta", "", [stickySlide: "left"]).opts.toMap()
        opts.get("stickySlide").should == "left"
        opts.get("meta").should == null
    }

    @Test
    void "parsing exception should provide details about plugin"() {
        code {
            pluginParamsFactory.create("file", 'hello {key: "value}')
        } should throwException("plugin parameters parsing error\n" +
                "  pluginId: file\n" +
                "  freeParamAndJsonText: hello {key: \"value}\n" +
                "  error: Unexpected end-of-input: was expecting closing quote for a string value\n" +
                " at [Source: (String)\"{key: \"value}\"; line: 1, column: 14]\n")
    }
}
