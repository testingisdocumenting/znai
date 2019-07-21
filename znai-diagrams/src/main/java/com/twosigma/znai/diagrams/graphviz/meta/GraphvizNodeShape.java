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

package com.twosigma.znai.diagrams.graphviz.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphvizNodeShape {
    private String shape;
    private String labelLoc;
    private Number width;
    private Number height;

    public GraphvizNodeShape(String shape, String labelLoc, Number width, Number height) {
        this.shape = shape;
        this.labelLoc = labelLoc;
        this.width = width;
        this.height = height;
    }

    public String getShape() {
        return shape;
    }

    public Number getWidth() {
        return width;
    }

    public Number getHeight() {
        return height;
    }

    public boolean isShapeSet() {
        return shape != null && !shape.isEmpty();
    }

    public boolean isLabelLocSet() {
        return labelLoc != null && !labelLoc.isEmpty();
    }

    public boolean isWidthSet() {
        return width != null;
    }

    public boolean isHeightSet() {
        return height != null;
    }

    public String asAttrs() {
        List<String> attrs = new ArrayList<>();
        if (isShapeSet()) {
            attrs.add("shape=" + shape);
        }

        if (isLabelLocSet()) {
            attrs.add("labelloc=" + labelLoc);
        }

        if (isWidthSet()) {
            attrs.add("width=" + width);
        }

        if (isHeightSet()) {
            attrs.add("height=" + height);
        }

        if (isWidthSet() || isHeightSet()) {
            attrs.add("fixedsize=true");
        }

        return String.join("; ", attrs);
    }

    @Override
    public String toString() {
        return asAttrs();
    }
}
