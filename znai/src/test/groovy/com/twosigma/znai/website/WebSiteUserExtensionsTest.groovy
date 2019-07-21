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

package com.twosigma.znai.website

import com.twosigma.znai.parser.TestResourceResolver
import org.junit.Test

import java.nio.file.Paths

import static java.util.stream.Collectors.toList

class WebSiteUserExtensionsTest {
    @Test
    void "should let specify extra web resources"() {
        createExtensions([:]).cssResources().collect(toList()).size().should == 0

        def extensions = createExtensions([
                cssResources: ['custom.css', 'another.css'],
                jsResources: ['custom.js', 'components.js'],
                jsClientOnlyResources: ['custom-client.js'],
                additionalFilesToDeploy: ['font1.woff2', 'font2.woff2'],
                htmlResources: ['custom.html']])

        def paths = { name -> extensions."$name"().collect(toList()).path }

        paths('cssResources').should == ['custom.css', 'another.css']
        paths('jsResources').should == ['custom.js', 'components.js']
        paths('jsClientOnlyResources').should == ['custom-client.js']
        paths('htmlResources').should == ['custom.html']
        paths('additionalFilesToDeploy').should == ['font1.woff2', 'font2.woff2']
    }

    private static WebSiteUserExtensions createExtensions(Map definition) {
        return new WebSiteUserExtensions(new TestResourceResolver(Paths.get('/dummy/root')),  definition)
    }
}
