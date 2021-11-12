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

package org.testingisdocumenting.znai.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeadingPayloadList {
    private final List<HeadingPayload> list;

    public HeadingPayloadList() {
        this.list = new ArrayList<>();
    }

    public HeadingPayloadList add(HeadingPayload payload) {
        list.add(payload);
        return this;
    }

    public List<Map<String, ?>> toListOfMaps() {
        return list.stream().map(HeadingPayload::toMap).collect(Collectors.toList());
    }
}
