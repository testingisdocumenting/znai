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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PythonCode {
    private final Map<String, PythonCodeEntry> entryByName;
    private final List<Map<String, Object>> parsed;
    private final PythonCodeContext context;

    private final Map<String, PythonClass> classByName;

    public PythonCode(List<Map<String, Object>> parsed, PythonCodeContext context) {
        this.parsed = parsed;
        this.context = context;
        entryByName = new LinkedHashMap<>();
        classByName = new HashMap<>();

        handleEntriesAndReturnMembers(parsed);
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

    public PythonClass findClassByName(String name) {
        return classByName.get(name);
    }

    public PythonCodeEntry findRequiredEntryByTypeAndName(String type, String name) {
        PythonCodeEntry entry = findEntryByName(name);
        if (entry == null) {
            throw new IllegalArgumentException("can't find entry: " + name);
        }

        if (!entry.getType().equals(type)) {
            throw new IllegalArgumentException("found entry by name <" + name + "> is not a " + type + ", but <" + entry.getType() + ">");
        }

        return entry;
    }

    public List<PythonCodeEntry> findAllEntriesWithPrefix(String prefix) {
        return entryByName.values().stream()
                .filter(e -> e.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<PythonCodeEntry> findAllEntriesByTypeWithPrefix(String type, String prefix) {
        return entryByName.values().stream()
                .filter(e -> e.getType().equals(type) && e.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<PythonCodeEntry> handleEntriesAndReturnMembers(List<Map<String, Object>> parsed) {
        List<PythonCodeEntry> result = new ArrayList<>();

        parsed.forEach(p -> {
            PythonCodeEntry entry = new PythonCodeEntry(p, context);
            entryByName.put(entry.getName(), entry);

            if (entry.getType().equals("class")) {
                handleClass(entry, (List<Map<String, Object>>) p.getOrDefault("members", Collections.emptyList()));
            } else {
                result.add(entry);
            }
        });

        return result;
    }

    private void handleClass(PythonCodeEntry entry, List<Map<String, Object>> parsedMembers) {
        PythonClass pythonClass = new PythonClass(entry.getName(), context);
        classByName.put(pythonClass.getName(), pythonClass);

        List<PythonCodeEntry> members = handleEntriesAndReturnMembers(parsedMembers);
        pythonClass.addMembers(members);
    }
}
