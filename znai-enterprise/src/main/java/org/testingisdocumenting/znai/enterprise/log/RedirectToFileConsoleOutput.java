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

package org.testingisdocumenting.znai.enterprise.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testingisdocumenting.znai.console.ConsoleOutput;
import org.testingisdocumenting.znai.console.ansi.IgnoreAnsiString;

public class RedirectToFileConsoleOutput implements ConsoleOutput {
    private static final Logger logger = createLogger();

    @Override
    public void out(Object... styleOrValues) {
        if (logger == null) {
            return;
        }

        logger.info(stripAnsi(styleOrValues));
    }

    @Override
    public void err(Object... styleOrValues) {
        if (logger == null) {
            return;
        }

        logger.error(stripAnsi(styleOrValues));
    }

    private static Logger createLogger() {
        String logPath = System.getProperty("logPath");
        if (logPath == null) {
            return null;
        }

        return LoggerFactory.getLogger(RedirectToFileConsoleOutput.class);
    }

    private static String stripAnsi(Object[] styleOrValues) {
        return new IgnoreAnsiString(styleOrValues).toString();
    }
}
