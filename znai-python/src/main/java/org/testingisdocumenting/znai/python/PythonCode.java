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

package org.testingisdocumenting.znai.python;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PythonCode {
    private final Map<String, PythonCodeEntry> entryByName;
    private final List<Map<String, Object>> parsed;

    public PythonCode(List<Map<String, Object>> parsed) {
        this.parsed = parsed;
        entryByName = new LinkedHashMap<>();
        parsed.forEach(p -> {
            PythonCodeEntry entry = new PythonCodeEntry(p);
            entryByName.put(entry.getName(), entry);
        });
    }

    public List<Map<String, Object>> getParsed() {
        return parsed;
    }

    public Stream<String> namesStream() {
        return entryByName.keySet().stream();
    }

    public PythonCodeEntry findEntryByName(String name) {
        return entryByName.get(name);
    }

    public List<PythonCodeEntry> findAllEntriesWithPrefix(String prefix) {
        return entryByName.values().stream()
                .filter(e -> e.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }
}
