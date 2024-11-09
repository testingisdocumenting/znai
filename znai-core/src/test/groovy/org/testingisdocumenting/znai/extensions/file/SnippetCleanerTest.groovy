/*
 * Copyright 2023 znai maintainers
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

package org.testingisdocumenting.znai.extensions.file

import org.junit.Test

class SnippetCleanerTest {
    @Test
    void "remove non ansi characters"() {
        SnippetCleaner.removeNonPrintable("helloWorld(a, b, c) 123").should == "helloWorld(a, b, c) 123"
        SnippetCleaner.removeNonPrintable(
                "\u001B[34mconfig: \u001B[33mscenarios/ui/webtau.cfg.groovy\u001B[0m // from command line argument\u001B[0m")
                .should == "config: scenarios/ui/webtau.cfg.groovy // from command line argument"

        SnippetCleaner.removeNonPrintable("\u001B]7;file://host.localdomain/Users/user\u0007\u001B[1m\u001B[7m%\u001B[27m\u001B[1m\u001B[0m")
                .should == "]7;file://host.localdomain/Users/user%"
    }
}
