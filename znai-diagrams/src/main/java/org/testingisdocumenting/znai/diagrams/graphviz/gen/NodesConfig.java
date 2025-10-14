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

package org.testingisdocumenting.znai.diagrams.graphviz.gen;

class NodesConfig {
    private Number width;
    private Number height;

    public NodesConfig() {
    }

    public NodesConfig(Number width, Number height) {
        this.width = width;
        this.height = height;
    }

    public Number getWidth() {
        return width;
    }

    public Number getHeight() {
        return height;
    }

    public boolean isWidthDefined(DiagramNode node) {
        return getWidth(node) != null;
    }

    public boolean isHeightDefined(DiagramNode node) {
        return getHeight(node) != null;
    }

    public boolean isSizeDefined(DiagramNode node) {
        return isWidthDefined(node) || isHeightDefined(node);
    }

    public Number getWidth(DiagramNode node) {
        Number nodeWidth = node.width();
        return nodeWidth != null ? nodeWidth : getWidth();
    }

    public Number getHeight(DiagramNode node) {
        Number nodeHeight = node.height();
        return nodeHeight != null ? nodeHeight : getHeight();
    }
}
