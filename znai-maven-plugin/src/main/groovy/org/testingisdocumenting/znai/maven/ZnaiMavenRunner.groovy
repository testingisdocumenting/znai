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

import org.testingisdocumenting.znai.console.ConsoleOutput
import org.testingisdocumenting.znai.console.ConsoleOutputs
import org.testingisdocumenting.znai.cli.ZnaiCliApp
import org.testingisdocumenting.znai.cli.ZnaiCliConfig

class ZnaiMavenRunner {
    static void run(ConsoleOutput consoleOutput, Map<String, String> argsMap) {
        ConsoleOutputs.add(consoleOutput)

        try {
            String[] args = constructArgs(argsMap)
            def config = new ZnaiCliConfig(args)

            ZnaiCliApp.start(config)
        } finally {
            ConsoleOutputs.remove(consoleOutput)
        }
    }

    static String[] constructArgs(Map<String, String> args) {
        return args.entrySet().stream()
        .map { entry -> argToString(entry) }
        .toArray { size -> new String[size] }
    }

    private static String argToString(Map.Entry<String, String> entry) {
        String str = "--" + entry.key
        if (entry.value != null) {
            str += "=" + entry.value
        }

        return str
    }
}
