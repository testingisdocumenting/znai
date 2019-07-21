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

package com.twosigma.znai.diagrams.graphviz.gen;

public class DiagramNode {
    private final String id;
    private final String label;
    private final String url;
    private final String colorGroup;
    private final String shape;
    private final Boolean highlight;

    public DiagramNode(String id, String label, String url, String colorGroup, String shape, Boolean highlight) {
        this.id = id;
        this.label = label;
        this.url = url;
        this.colorGroup = colorGroup;
        this.shape = shape;
        this.highlight = highlight;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getHighlight() {
        return highlight;
    }

    public String getColorGroup() {
        return colorGroup;
    }

    public String getShape() {
        return shape;
    }

    public Boolean hasUrl() {
        return !url.isEmpty();
    }

    public String getUrl() {
        return url;
    }
}
