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
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.*;

class CsvAnnotations {
    // type,beginX,beginY,endX,endY,text
    private static final int RECT_ARROW_TEXT_POS_IDX = 5;

    private int badgeNumber;
    private final NumberFormat numberFormat;
    private final MarkdownParser markdownParser;
    private final Path markupPath;
    private final BufferedImage image;
    private final Double pixelRatio;

    CsvAnnotations(MarkdownParser markdownParser, Path markupPath, BufferedImage image, Double pixelRatio) {
        this.markdownParser = markdownParser;
        this.markupPath = markupPath;
        this.image = image;
        this.pixelRatio = pixelRatio;
        badgeNumber = 1;
        numberFormat = NumberFormat.getNumberInstance();
    }

    List<Map<String, ?>> annotationsShapesFromCsv(String csvContent) {
        List<Map<String, ?>> annotations = new ArrayList<>();

        try (CSVParser csvRecords = readCsvRecords(csvContent)) {
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
        Map<String, Object> arrow = createArrowRectBaseMap("arrow", record);

        RectCoord rectCoord = new RectCoord(record);
        arrow.put("invertedColors",
                isDarkCoordinate(rectCoord.beginX, rectCoord.beginY) ||
                        isDarkCoordinate(rectCoord.endX, rectCoord.endY));

        arrow.putAll(rectCoord.toMap());

        return arrow;
    }

    private Map<String, Object> createRect(CSVRecord record) {
        Map<String, Object> rect = createArrowRectBaseMap("rectangle", record);

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

    private Map<String, Object> createArrowRectBaseMap(String type, CSVRecord record) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);

        // type,beginX,beginY,endX,endY,text
        if (record.isSet(RECT_ARROW_TEXT_POS_IDX)) {
            String markdown = record.get(RECT_ARROW_TEXT_POS_IDX).trim();
            MarkupParserResult parserResult = markdownParser.parse(markupPath, markdown);

            map.put("tooltip", parserResult.getDocElement().contentToListOfMaps());
        }

        return map;
    }

    private boolean isDarkCoordinate(Double x, Double y) {
        return ImageUtils.colorDarknessRatio(image,
                (int) (x * pixelRatio),
                (int) (y * pixelRatio),
                (int) (10 * pixelRatio)) > 0.5;
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
