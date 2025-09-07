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

package org.testingisdocumenting.znai.core;

import org.testingisdocumenting.znai.utils.JsonUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocMeta {
    public static final String META_FILE_NAME = "meta.json";

    private String id;

    private final String type;
    private final String title;
    private final String category;
    private final String description;
    private final List<String> allowedUsers;
    private final List<String> allowedGroups;
    private final boolean displayOnLanding;

    private boolean previewEnabled;
    private final Map<String, ?> docMetaMap;

    public DocMeta(String metaJson) {
        this(JsonUtils.deserializeAsMap(metaJson));
    }

    public DocMeta(Map<String, ?> docMetaMap) {
        this.id = stringValue(docMetaMap, "id", "");
        this.type = stringValue(docMetaMap, "type", "no-type");
        this.title = stringValue(docMetaMap, "title", "no-title");
        this.category = stringValue(docMetaMap, "category", "Un-categorized");
        this.displayOnLanding = booleanValue(docMetaMap, "displayOnLanding", true);
        this.description = stringValue(docMetaMap, "description", "no description");
        this.allowedUsers = stringList(docMetaMap, "allowedUsers");
        this.allowedGroups = stringList(docMetaMap, "allowedGroups");
        attachSlackMetaFromEnvVars(docMetaMap);

        this.docMetaMap = docMetaMap;
    }

    private void attachSlackMetaFromEnvVars(Map<String, ?> docMetaMap) {
        addMetaFromEnvVar(docMetaMap, "sendToSlackUrl", "ZNAI_SEND_TO_SLACK_URL");
        addMetaFromEnvVar(docMetaMap, "sendToSlackUrl", "ZNAI_SEND_TO_SLACK_URL");
    }

    public DocMeta cloneWithNewJson(String docMetaJson) {
        DocMeta clone = new DocMeta(docMetaJson);
        clone.setId(this.id);
        clone.setPreviewEnabled(this.previewEnabled);

        return clone;
    }

    /**
     * Documentation can be standalone and hosted by itself on a server. Or you may have multiple documentations
     * that are hosted within an organization. In later case it is important to have an id.
     *
     * @return documentation id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDisplayOnLanding() {
        return displayOnLanding;
    }

    public List<String> getAllowedUsers() {
        return allowedUsers;
    }

    public List<String> getAllowedGroups() {
        return allowedGroups;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("previewEnabled", previewEnabled);
        result.putAll(docMetaMap);

        return result;
    }

    public boolean isPreviewEnabled() {
        return previewEnabled;
    }

    public void setPreviewEnabled(boolean previewEnabled) {
        this.previewEnabled = previewEnabled;
    }

    private static String stringValue(Map<String, ?> docMetaMap, String key, String defaultValue) {
        return docMetaMap.containsKey(key) ? docMetaMap.get(key).toString() : defaultValue;
    }

    private static Boolean booleanValue(Map<String, ?> docMetaMap, String key, Boolean defaultValue) {
        return docMetaMap.containsKey(key) ? (Boolean) docMetaMap.get(key) : defaultValue;
    }

    @SuppressWarnings("unchecked")
    private static List<String> stringList(Map<String, ?> docMetaMap, String key) {
        Object value = docMetaMap.get(key);
        return value != null ? (List<String>) value : Collections.emptyList();
    }
}
