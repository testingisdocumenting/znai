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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Sections do not support bold text, images, bullet points, etc.
 * To customize headings style you can pass JSON block at the end of section text
 */
public record HeadingProps(Map<String, ?> props) {
    public static final String ANCHOR_ID_KEY = "customAnchorId";
    public static final String STYLE_KEY = "style";

    public static HeadingProps EMPTY = new HeadingProps(Collections.emptyMap());
    public static HeadingProps STYLE_API = new HeadingProps(Collections.singletonMap(STYLE_KEY, "api"));

    public static HeadingProps customAnchorId(String id) {
        Map<String, Object> props = new HashMap<>();
        props.put(ANCHOR_ID_KEY, id);

        return new HeadingProps(props);
    }
    public static HeadingProps styleApiWithBadge(String badgeText) {
        Map<String, Object> props = new HashMap<>();
        props.put("badge", badgeText);
        props.put(STYLE_KEY, "api");

        return new HeadingProps(props);
    }

    public boolean isCustomAnchorIdSet() {
        return props.containsKey(ANCHOR_ID_KEY);
    }

    public String getAnchorId() {
        return Objects.toString(props.get(ANCHOR_ID_KEY));
    }

    @Override
    public Map<String, ?> props() {
        return Collections.unmodifiableMap(props);
    }

    public String getStyle() {
        Object style = props.get(STYLE_KEY);
        return style == null ? "" : style.toString();
    }
}
