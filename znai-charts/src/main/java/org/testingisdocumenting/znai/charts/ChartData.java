/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ChartData {
    private final List<String> labels;
    private final List<List<Object>> data;

    public ChartData(List<String> labels, List<List<Object>> data) {
        this.labels = labels;
        this.data = data;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);

        return result;
    }
}
