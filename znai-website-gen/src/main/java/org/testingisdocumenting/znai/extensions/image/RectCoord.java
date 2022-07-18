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

import org.apache.commons.csv.CSVRecord;

import java.util.LinkedHashMap;
import java.util.Map;

class RectCoord {
    private final Number beginX;
    private final Number beginY;
    private final Number endX;
    private final Number endY;

    RectCoord(CSVRecord record) {
        beginX = Double.parseDouble(record.get(1));
        beginY = Double.parseDouble(record.get(2));
        endX = Double.parseDouble(record.get(3));
        endY = Double.parseDouble(record.get(4));
    }

    public double getBeginX() {
        return beginX.doubleValue();
    }

    public double getBeginY() {
        return beginY.doubleValue();
    }

    public double getEndX() {
        return endX.doubleValue();
    }

    public double getEndY() {
        return endY.doubleValue();
    }

    RectCoord(Map<String, ?> shape) {
        beginX = (Number) shape.get("beginX");
        beginY = (Number) shape.get("beginY");
        endX = (Number) shape.get("endX");
        endY = (Number) shape.get("endY");
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