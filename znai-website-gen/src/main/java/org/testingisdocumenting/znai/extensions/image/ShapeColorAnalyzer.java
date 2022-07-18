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

import java.awt.image.BufferedImage;

class ShapeColorAnalyzer {
    private final BufferedImage image;
    private final Double pixelRatio;

    ShapeColorAnalyzer(BufferedImage image, Double pixelRatio) {
        this.image = image;
        this.pixelRatio = pixelRatio;
    }

    boolean isDarkCoordinate(Number x, Number y) {
        return ImageUtils.colorDarknessRatio(image,
                (int) (x.doubleValue() * pixelRatio),
                (int) (y.doubleValue() * pixelRatio),
                (int) (10 * pixelRatio)) > 0.5;
    }

    boolean isDarkBasedOnOppositeCorners(RectCoord rectCoord) {
        return isDarkCoordinate(rectCoord.getBeginX(), rectCoord.getBeginY()) ||
                isDarkCoordinate(rectCoord.getEndX(), rectCoord.getEndY());
    }

    boolean isDarkBasedOnAllCorners(RectCoord rectCoord) {
        int numberOfDarkAreas = 0;
        if (isDarkCoordinate(rectCoord.getBeginX(), rectCoord.getBeginY())) {
            numberOfDarkAreas++;
        }
        if (isDarkCoordinate(rectCoord.getBeginX(), rectCoord.getEndY())) {
            numberOfDarkAreas++;
        }
        if (isDarkCoordinate(rectCoord.getEndX(), rectCoord.getBeginY())) {
            numberOfDarkAreas++;
        }
        if (isDarkCoordinate(rectCoord.getEndX(), rectCoord.getEndY())) {
            numberOfDarkAreas++;
        }

        return numberOfDarkAreas >= 2;
    }
}
