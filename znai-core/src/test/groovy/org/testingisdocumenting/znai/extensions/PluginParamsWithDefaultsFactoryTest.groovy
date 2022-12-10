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

package org.testingisdocumenting.znai.extensions

import org.junit.Test
import org.testingisdocumenting.znai.structure.PageMeta

class PluginParamsWithDefaultsFactoryTest {
    @Test
    void "global defaults values"() {
        def paramsFactory = new PluginParamsWithDefaultsFactory()
        paramsFactory.setGlobalParams(["file": ["autoTitle": true]])

        def pluginParams = paramsFactory.create("file", "", [anchorId: "my-anchor"])
        pluginParams.opts.toMap().should == [anchorId: "my-anchor", autoTitle: true]
    }

    @Test
    void "page local default values"() {
        def paramsFactory = new PluginParamsWithDefaultsFactory()
        def pageMeta = new PageMeta(["title": ["my-title"], "file": ["{autoTitle: true}"]])
        paramsFactory.setPageLocalParams(pageMeta)

        def pluginParams = paramsFactory.create("file", "", [anchorId: "my-anchor"])
        pluginParams.opts.toMap().should == [anchorId: "my-anchor", autoTitle: true]
    }

    @Test
    void "local default values takes precedent"() {
        def paramsFactory = new PluginParamsWithDefaultsFactory()
        paramsFactory.setGlobalParams(["file": ["autoTitle": false]])

        def pageMeta = new PageMeta(["title": ["my-title"], "file": ["{autoTitle: true}"]])
        paramsFactory.setPageLocalParams(pageMeta)

        def pluginParams = paramsFactory.create("file", "", [anchorId: "my-anchor"])
        pluginParams.opts.toMap().should == [anchorId: "my-anchor", autoTitle: true]
    }
}
