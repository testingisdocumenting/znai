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

package com.twosigma.znai.structure;

import com.twosigma.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class DocMeta {
    private String id;
    private String type;
    private String title;
    private boolean previewEnabled;
    private Map<String, ?> docMetaMap;

    public DocMeta(String metaJson) {
        this(JsonUtils.deserializeAsMap(metaJson));
    }

    public DocMeta(Map<String, ?> docMetaMap) {
        this.type = docMetaMap.containsKey("type") ? docMetaMap.get("type").toString() : "no-type";
        this.title = docMetaMap.containsKey("title") ? docMetaMap.get("title").toString() : "no-title";
        this.docMetaMap = docMetaMap;
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
}
