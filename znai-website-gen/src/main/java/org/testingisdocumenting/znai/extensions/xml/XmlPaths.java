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

package org.testingisdocumenting.znai.extensions.xml;

import java.util.*;

class XmlPaths {
    private final Set<String> paths;

    XmlPaths(Map<String, ?> xmlAsJson) {
        paths = new LinkedHashSet<>();
        buildPaths(getTagName(xmlAsJson), xmlAsJson);
    }

    public Set<String> getPaths() {
        return paths;
    }

    private void buildPaths(String path, Map<String, ?> xmlAsJson) {
        paths.add(path);
        List<Map<String, ?>> children = getChildren(xmlAsJson);

        if (children.size() == 1) {
            String tagName = getTagName(children.get(0));
            if (!tagName.isEmpty()) {
                buildPaths(path + "." + tagName, children.get(0));
            }
        } else {
            buildPathsForChildren(path, children);
        }
    }

    private void buildPathsForChildren(String path, List<Map<String, ?>> children) {
        Map<String, Integer> idxByTag = new HashMap<>();
        for (Map<String, ?> child : children) {
            String tagName = getTagName(child);
            if (tagName.isEmpty()) {
                continue;
            }

            Integer idx = idxByTag.get(tagName);
            if (idx == null) {
                idx = 0;
            } else {
                idx++;
            }
            idxByTag.put(tagName, idx);
            buildPaths(path + "." + tagName + "[" + idx + "]", child);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, ?>> getChildren(Map<String, ?> node) {
        Object children = node.get("children");
        if (children == null) {
            return Collections.emptyList();
        }

        return (List<Map<String, ?>>) children;
    }

    private String getTagName(Map<String, ?> node) {
        return node.get("tagName").toString();
    }
}