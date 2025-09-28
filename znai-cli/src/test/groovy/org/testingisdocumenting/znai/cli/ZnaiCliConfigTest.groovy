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

package org.testingisdocumenting.znai.cli

import org.junit.Test

class ZnaiCliConfigTest {
    @Test
    void "legacy mode as string"() {
        mode('--deploy=location').should == 'build'
        mode('--doc-id=my-doc').should == 'build'
        mode('--preview').should == 'preview'
        mode('--export=my-dir').should == 'export'
    }

    @Test
    void "new command mode as string"() {
        mode('build', '--deploy=location').should == 'build'
        mode('build', '--doc-id=my-doc').should == 'build'
        mode('preview').should == 'preview'
        mode('preview', '--port=4000').should == 'preview'
        mode('export', 'my-dir').should == 'export'
        mode('new').should == 'scaffold new'
        mode('serve').should == 'serve'
    }

    @Test
    void "defaults to build when no command specified"() {
        mode('--source=docs').should == 'build'
        mode('some-path').should == 'build'
    }

    private static String mode(String... args) {
        return new ZnaiCliConfig(args).modeAsString
    }
}
