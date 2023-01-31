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

package org.testingisdocumenting.znai.extensions.json;

import java.util.*;

class JsonPaths {
    private final Set<String> paths;

    JsonPaths(Object json) {
        paths = new LinkedHashSet<>();
        buildPaths("root", json);
    }

    public Set<String> getPaths() {
        return paths;
    }

    private void buildPaths(String path, Object json) {
        paths.add(path);
        handleList(path, json);
        handleMap(path, json);
    }

    @SuppressWarnings("unchecked")
    private void handleList(String path, Object json) {
        if (!(json instanceof List)) {
            return;
        }

        List<Object> list = (List<Object>) json;
        int idx = 0;
        for (Object v : list) {
            buildPaths(path + "[" + idx + "]", v);
            idx++;
        }
    }

    @SuppressWarnings("unchecked")
    private void handleMap(String path, Object json) {
        if (!(json instanceof Map)) {
            return;
        }

        Map<String, Object> map = (Map<String, Object>) json;
        map.forEach((k, v) -> buildPaths(path + "." + k, v));
    }
}