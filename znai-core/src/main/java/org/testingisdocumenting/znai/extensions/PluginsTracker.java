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

package org.testingisdocumenting.znai.extensions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

class PluginsTracker {
    public final Tracker includePlugins = new Tracker();
    public final Tracker fencePlugins = new Tracker();
    public final Tracker inlineCodePlugins = new Tracker();

    public Map<String, ?> buildStatsMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("includePlugins", includePlugins.buildStats());
        result.put("fencePlugins", fencePlugins.buildStats());
        result.put("inlineCodePlugins", inlineCodePlugins.buildStats());

        return result;
    }

    static class Tracker {
        private final Map<String, LongAdder> countByName = new ConcurrentHashMap<>();

        public void increment(String pluginId) {
            countByName.computeIfAbsent(pluginId, k -> new LongAdder()).increment();
        }

        public Map<String, Integer> buildStats() {
            return countByName.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));
        }
    }
}
