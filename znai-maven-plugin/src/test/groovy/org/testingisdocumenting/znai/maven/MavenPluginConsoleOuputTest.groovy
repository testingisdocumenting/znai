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

package org.testingisdocumenting.znai.maven

import org.testingisdocumenting.znai.console.ansi.Color
import org.junit.Test

class MavenPluginConsoleOutputTest {
    @Test
    void "should ignore colours and invoke toString"() {
        def output = MavenPluginConsoleOuput.concatIgnoringColours("foo", Color.RED, "bar", 123)
        output.should == 'foobar123'
    }
}
