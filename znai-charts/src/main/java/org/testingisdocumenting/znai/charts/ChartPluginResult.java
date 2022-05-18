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

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;

import java.util.LinkedHashMap;
import java.util.Map;

class ChartPluginResult {
    private ChartPluginResult() {
    }

    static PluginResult create(PluginParams pluginParams, String type, String csvContent) {
        ChartData chartData = ChartDataCsvParser.parse(csvContent);

        Map<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        props.put("chartType", type);
        props.putAll(chartData.toMap());

        return PluginResult.docElement("EchartGeneric", props);
    }
}
