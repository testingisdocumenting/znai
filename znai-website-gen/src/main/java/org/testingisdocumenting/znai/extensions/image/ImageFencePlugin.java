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
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ImageFencePlugin extends ImagePluginBase implements FencePlugin {
    private String content;

    @Override
    public FencePlugin create() {
        return new ImageFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.content = content;
        return process(componentsRegistry, pluginParams);
    }

    @Override
    protected List<Map<String, ?>> annotationShapes() {
        List<Map<String, ?>> badges = new ArrayList<>();

        CSVParser csvRecords = readCsvRecords(content);
        int badgeNumber = 1;
        for (CSVRecord record : csvRecords) {
            String xText = record.get(0);
            String yText = record.get(1);

            Map<String, Object> badge = new HashMap<>();
            badge.put("type", "badge");
            badge.put("x", toNum(xText));
            badge.put("y", toNum(yText));
            badge.put("text", String.valueOf(badgeNumber));

            badges.add(badge);

            badgeNumber++;
        }

        return badges;
    }

    @Override
    protected Double pixelRatio() {
        return 1.0;
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
}
