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

package org.testingisdocumenting.znai.parser

import org.testingisdocumenting.znai.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.znai.console.ansi.IgnoreAnsiString
import org.testingisdocumenting.znai.core.Log

class TestLog implements Log {
    private final List<String> warnings = []

    void clear() {
        warnings.clear()
    }

    List<String> getWarnings() {
        return warnings
    }

    @Override
    void phase(String message) {

    }

    @Override
    void info(Object... styleOrValue) {

    }

    @Override
    void warn(Object... styleOrValue) {
        println new AutoResetAnsiString(styleOrValue).toString()
        warnings.add(new IgnoreAnsiString(styleOrValue).toString())
    }
}
