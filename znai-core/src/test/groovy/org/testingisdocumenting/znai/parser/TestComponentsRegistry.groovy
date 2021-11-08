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

package org.testingisdocumenting.znai.parser

import org.testingisdocumenting.znai.core.ComponentsRegistry
import org.testingisdocumenting.znai.core.DocConfig
import org.testingisdocumenting.znai.core.GlobalAssetsRegistry
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.testingisdocumenting.znai.resources.ResourcesResolver
import org.testingisdocumenting.znai.time.FakeTimeService
import org.testingisdocumenting.znai.time.TimeService

import java.nio.file.Paths

class TestComponentsRegistry implements ComponentsRegistry {
    public static final TestComponentsRegistry TEST_COMPONENTS_REGISTRY = new TestComponentsRegistry()

    private final TestDocStructure docStructure = new TestDocStructure()
    private final MarkupParser markdownParser = new TestMarkdownParser()

    private MarkupParser defaultParser = new TestMarkupParser()

    private GlobalAssetsRegistry assetsRegistry = new GlobalAssetsRegistry()
    private TimeService timeService = new FakeTimeService()

    private DocConfig docConfig = new DocConfig(Paths.get("").toAbsolutePath())

    TestComponentsRegistry() {
    }

    void setDefaultParser(MarkupParser defaultParser) {
        this.defaultParser = defaultParser
    }

    @Override
    MarkupParser defaultParser() {
        return defaultParser
    }

    @Override
    MarkdownParser markdownParser() {
        return markdownParser
    }

    @Override
    ResourcesResolver resourceResolver() {
        return new TestResourceResolver(Paths.get(""))
    }

    @Override
    TestDocStructure docStructure() {
        return docStructure
    }

    @Override
    DocConfig docConfig() {
        return docConfig
    }

    @Override
    GlobalAssetsRegistry globalAssetsRegistry() {
        return assetsRegistry
    }

    @Override
    FakeTimeService timeService() {
        return timeService
    }
}