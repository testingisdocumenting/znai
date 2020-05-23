/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.enterprise.monitoring

import org.junit.Test

class MonitorConfigTest {
    @Test
    void "should maintain a list of roots and associated wild card patterns"() {
        def config = new MonitorConfig([
                "intervalMillis": 20000,
                "paths": [[
                     "rootDir": "/home/fake-build-dir",
                     "wildCardPatterns": ["*/*.zip", "inner/*/*.zip"]]]])

        config.intervalMillis.should == 20000

        println config.buildRootsAndPatterns
        config.buildRootsAndPatterns.size().should == 1
        config.buildRootsAndPatterns[0].buildRoot.toString().should == "/home/fake-build-dir"
        config.buildRootsAndPatterns[0].wildCardPatterns.should == ["*/*.zip", "inner/*/*.zip"]
    }
}
