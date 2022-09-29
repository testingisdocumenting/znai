/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.parser.table;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Row {
    private final List<Object> data;

    public Row() {
        data = new ArrayList<>();
    }

    Row(List<Object> data) {
        this.data = data;
    }

    public void add(Object v) {
        data.add(v);
    }

    public List<Object> getData() {
        return data;
    }

    public Row map(MarkupTableDataMapping mapping) {
        return new Row(data.stream().map(v -> {
            Object newValue = mapping.map(v);
            return newValue == null ? v : newValue;
        }).collect(toList()));
    }

    public boolean matchRegexp(Pattern regexp) {
        return data.stream()
                .anyMatch(v -> regexp.matcher(v.toString()).find());
    }

    @SuppressWarnings("unchecked")
    public <E> E get(int idx) {
        return (E) data.get(idx);
    }

    public Row onlyWithIdxs(List<Integer> idxs) {
        return new Row(idxs.stream().map(data::get).collect(toList()));
    }
}
