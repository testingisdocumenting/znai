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

package org.testingisdocumenting.znai.parser.sphinx.python;

import java.util.LinkedHashMap;
import java.util.Map;

public class PythonFunctionParamSignature {
    private String givenExample;
    private boolean isOptional;

    public PythonFunctionParamSignature(String givenExample, boolean isOptional) {
        this.givenExample = givenExample;
        this.isOptional = isOptional;
    }

    public String getGivenExample() {
        return givenExample;
    }

    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public String toString() {
        return "PythonFunctionParamSignature{" +
                "givenExample='" + givenExample + '\'' +
                ", isOptional=" + isOptional +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("givenExample", givenExample);
        result.put("isOptional", isOptional);

        return result;
    }
}
