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

public class PythonParsedFile {
    private final Map<String, PythonParsedEntry> entryByName;
    private final List<Map<String, Object>> parsed;
    private final PythonContext context;

    private final Map<String, PythonClass> classByName;

    public PythonParsedFile(List<Map<String, Object>> parsed, PythonContext context) {
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

    public PythonParsedEntry findEntryByName(String name) {
        return entryByName.get(name);
    }

    public PythonClass findClassByName(String name) {
        return classByName.get(name);
    }

    public PythonParsedEntry findRequiredEntryByTypeAndName(String type, String name) {
        PythonParsedEntry entry = findEntryByName(name);
        if (entry == null) {
            throw new IllegalArgumentException("can't find entry: " + name);
        }

        if (!entry.getType().equals(type)) {
            throw new IllegalArgumentException("found entry by name <" + name + "> is not a " + type + ", but <" + entry.getType() + ">");
        }

        return entry;
    }

    public List<PythonParsedEntry> findAllEntriesWithPrefix(String prefix) {
        return entryByName.values().stream()
                .filter(e -> e.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<PythonParsedEntry> findAllEntriesByTypeWithPrefix(String type, String prefix) {
        return entryByName.values().stream()
                .filter(e -> e.getType().equals(type) && e.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<PythonParsedEntry> handleEntriesAndReturnMembers(List<Map<String, Object>> parsed) {
        List<PythonParsedEntry> result = new ArrayList<>();

        parsed.forEach(p -> {
            PythonParsedEntry entry = new PythonParsedEntry(p, context);
            entryByName.put(entry.getName(), entry);

            if (entry.getType().equals("class")) {
                handleClass(entry, (List<Map<String, Object>>) p.getOrDefault("members", Collections.emptyList()));
            } else {
                result.add(entry);
            }
        });

        return result;
    }

    private void handleClass(PythonParsedEntry entry, List<Map<String, Object>> parsedMembers) {
        PythonClass pythonClass = new PythonClass(entry.getName(), context);
        classByName.put(pythonClass.getName(), pythonClass);

        List<PythonParsedEntry> members = handleEntriesAndReturnMembers(parsedMembers);
        pythonClass.addMembers(members);
    }
}
