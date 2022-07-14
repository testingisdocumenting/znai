/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.image;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Stream;

public class ImageFencePlugin extends ImagePluginBase implements FencePlugin {
    private String content;
    private BufferedImage image;
    private Double pixelRatio;
    private int badgeNumber;

    private NumberFormat numberFormat;

    @Override
    public FencePlugin create() {
        return new ImageFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.content = content;
        return process(componentsRegistry, markupPath, pluginParams);
    }

    @Override
    protected List<Map<String, ?>> annotationShapes(BufferedImage image) {
        this.image = image;

        List<Map<String, ?>> annotations = new ArrayList<>();

        pixelRatio = pixelRatio();
        badgeNumber = 1;

        numberFormat = NumberFormat.getNumberInstance();

        try (CSVParser csvRecords = readCsvRecords(content)) {
            for (CSVRecord record : csvRecords) {
                Map<String, Object> annotation = createAnnotation(record);
                annotations.add(annotation);

            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return annotations;
    }

    private Map<String, Object> createAnnotation(CSVRecord record) {
        String xCoordOrType = record.get(0);
        if (StringUtils.isNumeric(numberFormat, xCoordOrType)) {
            return createBadge(record);
        }

        switch (xCoordOrType) {
            case "arrow":
                return createArrow(record);
            case "rect":
                return createRect(record);
            default:
                throw new IllegalArgumentException("unsupported annotation type: " + xCoordOrType);
        }
    }

    private Map<String, Object> createBadge(CSVRecord record) {
        String xText = record.get(0);
        String yText = record.get(1);

        Map<String, Object> badge = new HashMap<>();
        badge.put("type", "badge");

        Double x = toNum(xText);
        Double y = toNum(yText);

        badge.put("x", x);
        badge.put("y", y);
        badge.put("text", String.valueOf(badgeNumber));
        badge.put("invertedColors", isDarkCoordinate(x, y));

        badgeNumber++;

        return badge;
    }

    private Map<String, Object> createArrow(CSVRecord record) {
        Map<String, Object> arrow = new HashMap<>();
        arrow.put("type", "arrow");

        RectCoord rectCoord = new RectCoord(record);
        arrow.put("invertedColors",
                isDarkCoordinate(rectCoord.beginX, rectCoord.beginY) ||
                isDarkCoordinate(rectCoord.endX, rectCoord.endY));

        arrow.putAll(rectCoord.toMap());

        return arrow;
    }

    private Map<String, Object> createRect(CSVRecord record) {
        Map<String, Object> rect = new HashMap<>();
        rect.put("type", "rectangle");

        RectCoord rectCoord = new RectCoord(record);

        int numberOfDarkAreas = 0;
        if (isDarkCoordinate(rectCoord.beginX, rectCoord.beginY)) {
            numberOfDarkAreas++;
        }
        if (isDarkCoordinate(rectCoord.beginX, rectCoord.endY)) {
            numberOfDarkAreas++;
        }
        if (isDarkCoordinate(rectCoord.endX, rectCoord.beginY)) {
            numberOfDarkAreas++;
        }
        if (isDarkCoordinate(rectCoord.endX, rectCoord.endY)) {
            numberOfDarkAreas++;
        }

        rect.put("invertedColors", numberOfDarkAreas >= 2);
        rect.putAll(rectCoord.toMap());

        return rect;
    }

    private boolean isDarkCoordinate(Double x, Double y) {
        return ImageUtils.colorDarknessRatio(image,
                (int) (x * pixelRatio),
                (int) (y * pixelRatio),
                (int) (10 * pixelRatio)) > 0.5;
    }

    @Override
    protected Double pixelRatio() {
        if (pixelRatioFromOpts != null) {
            return pixelRatioFromOpts;
        }

        return 1.0;
    }

    @Override
    protected PluginParamsDefinition additionalParameters() {
        return PluginParamsDefinition.undefined();
    }

    @Override
    protected Stream<AuxiliaryFile> additionalAuxiliaryFiles() {
        return Stream.empty();
    }

    private static Double toNum(String text) {
        if (text.isEmpty()) {
            return 0.0;
        }

        return Double.parseDouble(text);
    }

    private static CSVParser readCsvRecords(String content) {
        try {
            return CSVFormat.RFC4180.
                    withIgnoreSurroundingSpaces().
                    withIgnoreEmptyLines().
                    withTrim().
                    withDelimiter(',').
                    parse(new StringReader(content));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class RectCoord {
        private final double beginX;
        private final double beginY;
        private final double endX;
        private final double endY;

        RectCoord(CSVRecord record) {
            beginX = toNum(record.get(1));
            beginY = toNum(record.get(2));
            endX = toNum(record.get(3));
            endY = toNum(record.get(4));
        }

        Map<String, ?> toMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("beginX", beginX);
            map.put("beginY", beginY);
            map.put("endX", endX);
            map.put("endY", endY);

            return map;
        }
    }
}
