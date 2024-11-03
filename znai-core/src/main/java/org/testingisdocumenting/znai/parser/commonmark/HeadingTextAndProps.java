/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.parser.commonmark;

import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.utils.JsonParseException;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.util.Collections;
import java.util.Map;

public record HeadingTextAndProps(String text, HeadingProps props) {
    public static HeadingTextAndProps extractTextAndProps(String text) {
        int startOfCurlyIdx = text.indexOf('{');
        int endOfCurlyIdx = text.lastIndexOf('}');
        if (startOfCurlyIdx == -1 || endOfCurlyIdx == -1) {
            return new HeadingTextAndProps(text, HeadingProps.EMPTY);
        }

        String json = text.substring(startOfCurlyIdx);
        // empty braces
        if (json.length() < 3) {
            return new HeadingTextAndProps(text, HeadingProps.EMPTY);
        }

        Map<String, ?> props = json.charAt(1) == '#' ?
                parseCustomAnchorId(json):
                parseJson(json);

        String headingTextOnly = text.substring(0, startOfCurlyIdx);
        return new HeadingTextAndProps(headingTextOnly, new HeadingProps(props));
    }

    private static Map<String, ?> parseCustomAnchorId(String anchorExpression) {
        int endOfCurlyIdx = anchorExpression.lastIndexOf('}');
        var anchorId = anchorExpression.substring(2, endOfCurlyIdx);
        return Collections.singletonMap(HeadingProps.ANCHOR_ID_KEY, anchorId);
    }

    private static Map<String, ?> parseJson(String json) {
        try {
            return JsonUtils.deserializeAsMap(json);
        } catch (JsonParseException e) {
            throw new RuntimeException("Can't parse props of heading: " + json, e);
        }
    }
}
