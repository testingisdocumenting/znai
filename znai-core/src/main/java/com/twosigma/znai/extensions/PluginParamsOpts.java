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

package com.twosigma.znai.extensions;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class PluginParamsOpts {
    private Map<String, ?> opts;

    PluginParamsOpts(Map<String, ?> opts) {
        this.opts = opts;
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String name) {
        return (E) opts.get(name);
    }

    public <E> E get(String name, E defaultValue) {
        return has(name) ? get(name) : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> getList(String name) {
        if (!has(name)) {
            return Collections.emptyList();
        }

        Object v = get(name);
        if (!(v instanceof List)) {
            E casted = (E) v;
            return Collections.singletonList(casted);
        }

        return (List<E>) v;
    }

    public <E> Set<E> getSet(String name) {
        return new HashSet<E>(getList(name));
    }

    public Stream<String> getNames() {
        return opts.keySet().stream();
    }

    public void forEach(BiConsumer<String, Object> consumer) {
        opts.forEach(consumer);
    }

    public String getString(String name) {
        Object v = opts.get(name);
        if (v == null) {
            return null;
        }

        return v.toString();
    }

    public String getRequiredString(String name) {
        Object v = opts.get(name);
        if (v == null) {
            throw new RuntimeException("'" + name + "' is required");
        }

        return v.toString();
    }

    public boolean has(String name) {
        return opts.containsKey(name);
    }

    public boolean isEmpty() {
        return opts.isEmpty();
    }

    public Map<String, Object> toMap() {
        return new LinkedHashMap<>(opts);
    }
}
