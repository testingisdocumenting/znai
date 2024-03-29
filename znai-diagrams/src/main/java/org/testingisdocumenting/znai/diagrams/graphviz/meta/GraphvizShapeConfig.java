/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.diagrams.graphviz.meta;

import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;
import org.testingisdocumenting.znai.utils.ResourceUtils;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * We can assign a node style by adding an extra information to label
 * <pre>
 *     node [label="text [a database]"];
 * </pre>
 *
 * In the example above two styles are set. A style can change a color scheme.
 * Also style can change a shape. Graphviz has predefined shapes that can be specified explicitly.
 * Instead of doing it explicitly though, we will specify it during preprocessing and at the
 * final rendering will replace that shape with a custom svg.
 *
 * So 'database' style may be auto rendered as a SVG showing a data storage. And in order to make all
 * the lines connect properly a shape must be of a proper height, width and if possible, shape.
 */
public class GraphvizShapeConfig {
    private final Map<String, ?> config;

    private final Map<String, String> svgByName;

    public GraphvizShapeConfig(String json) {
        this(JsonUtils.deserializeAsMap(json));
    }
    public GraphvizShapeConfig(Map<String, ?> config) {
        this.config = config;
        this.svgByName = buildSvgByName();
    }

    public Optional<String> shapeSvg(String style) {
        if (svgByName != null) {
            return Optional.ofNullable(svgByName.get(style));
        }

        Map<String, ?> c = getSubMap(style);
        if (c == null) {
            return Optional.empty();
        }

        String svg = getValue(c, "svg");
        if (svg != null) {
            return Optional.of(svg);
        }

        String svgPath = getValue(c, "svgPath");
        if (svgPath != null) {
            return loadSvg(svgPath);
        }

        return Optional.empty();
    }

    public boolean isInvertedTextColorByStyleId(String style) {
        Map<String, ?> c = getSubMap(style);
        if (c == null) {
            return false;
        }

        Boolean invertedText = getValue(c, "invertedText");
        return invertedText != null;
    }

    public Optional<GraphvizNodeShape> nodeShape(String style) {
        Map<String, ?> c = getSubMap(style);
        if (c == null) {
            return Optional.empty();
        }

        String shape = getValue(c, "shape");
        String labelLoc = getValue(c, "labelloc");
        Number width = getValue(c, "width");
        Number height = getValue(c, "height");

        return Optional.of(new GraphvizNodeShape(shape, labelLoc, width, height));
    }

    public Map<String, ?> getSvgByName() {
        return svgByName;
    }

    private Map<String, String> buildSvgByName() {
        Map<String, String> result = new HashMap<>();
        config.forEach((id, styleDef)  -> {
            Optional<String> svg = shapeSvg(id);
            if (!svg.isPresent()) {
                throw new RuntimeException("no svg found for <" + id + ">");
            }

            result.put(id, svg.get());
        });

        return result;
    }

    @SuppressWarnings("unchecked")
    private static <E> E getValue(Map<String, ?> map, String name) {
        return (E) map.get(name);
    }

    private Optional<String> loadSvg(String svgPath) {
        String svgFromResource = ResourceUtils.textContent(svgPath);
        if (svgFromResource != null) {
            return Optional.of(svgFromResource);
        }

        String svgFromFs = FileUtils.fileTextContent(Paths.get(svgPath));
        return Optional.ofNullable(svgFromFs);
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> getSubMap(String style) {
        return (Map<String, ?>) config.get(style);
    }
}
