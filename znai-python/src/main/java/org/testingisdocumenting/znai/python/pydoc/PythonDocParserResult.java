/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.python.pydoc;

import org.testingisdocumenting.znai.python.PythonDocParam;

import java.util.List;
import java.util.Optional;

public class PythonDocParserResult {
    private final String descriptionOnly;
    private final List<PythonDocParam> params;
    private final PythonDocReturn funcReturn;

    public PythonDocParserResult(String descriptionOnly, List<PythonDocParam> params, PythonDocReturn funcReturn) {
        this.descriptionOnly = descriptionOnly;
        this.params = params;
        this.funcReturn = funcReturn;
    }

    public String getDescriptionOnly() {
        return descriptionOnly;
    }

    public List<PythonDocParam> getParams() {
        return params;
    }

    /**
     *
     * @return pyth
     */
    public PythonDocReturn getFuncReturn() {
        return funcReturn;
    }
}
