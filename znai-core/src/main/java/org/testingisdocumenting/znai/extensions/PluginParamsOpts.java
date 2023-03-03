/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class PluginParamsOpts {
    private final String pluginId;
    private final Map<String, ?> opts;
    private final Map<String, String> renamesOldByNewName;

    PluginParamsOpts(String pluginId, Map<String, ?> opts) {
        this.pluginId = pluginId;
        this.opts = opts;
        this.renamesOldByNewName = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String name) {
        String nameToUse = renamesOldByNewName.getOrDefault(name, name);
        return (E) opts.get(nameToUse);
    }

    public <E> E get(String name, E defaultValue) {
        return has(name) ? get(name) : defaultValue;
    }

    public Number getNumber(String name, Number defaultValue) {
        return get(name, defaultValue);
    }

    public Number getNumber(String name) {
        return get(name);
    }

    public void assignToProps(Map<String, ?> props, String name) {
        if (has(name)) {
            props.put(name, get(name));
        }
    }

    public void assignToProps(Map<String, ?> props) {
        opts.keySet().forEach(k -> props.put(k, get(k)));
    }

    public void assignToDocElement(DocElement docElement) {
        opts.forEach(docElement::addProp);
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
        return new HashSet<>(getList(name));
    }

    public Stream<String> getNames() {
        return opts.keySet().stream();
    }

    public void forEach(BiConsumer<String, Object> consumer) {
        opts.forEach(consumer);
    }

    public String getString(String name) {
        Object v = get(name);
        if (v == null) {
            return null;
        }

        return v.toString();
    }

    public String getRequiredString(String name) {
        Object v = get(name);
        if (v == null) {
            throw new RuntimeException("'" + name + "' is required for plugin: " + pluginId);
        }

        return v.toString();
    }

    public boolean has(String name) {
        String nameToUse = renamesOldByNewName.getOrDefault(name, name);
        return opts.containsKey(nameToUse);
    }

    public boolean isEmpty() {
        return opts.isEmpty();
    }

    public Map<String, Object> toMap() {
        return new LinkedHashMap<>(opts);
    }

    @Override
    public String toString() {
        return "PluginParamsOpts{" +
                "pluginId='" + pluginId + '\'' +
                ", opts=" + opts +
                '}';
    }

    public void setRenamesInfo(Map<String, String> renamesNewByOldName) {
        this.renamesOldByNewName.clear();
        this.renamesOldByNewName.putAll(renamesNewByOldName);
    }
}
