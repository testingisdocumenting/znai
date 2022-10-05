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

public class ImageUtils {
    private ImageUtils() {
    }

    public static double colorDarknessRatio(int rgb) {
        int red = (rgb >> 16) & 0x000000FF;
        int green = (rgb >>8 ) & 0x000000FF;
        int blue = (rgb) & 0x000000FF;

        // borrowed from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
        return 1 - (0.299 * red + 0.587 * green + 0.114 * blue) / 255.0;
    }

    public static double colorDarknessRatio(BufferedImage image, int x, int y, int sampleSize) {
        int halfSample = sampleSize / 2;
        return colorDarknessRatio(image, x - halfSample, y - halfSample, x + halfSample, y + halfSample);
    }

    public static double colorDarknessRatio(BufferedImage image, int x1, int y1, int x2, int y2) {
        x1 = Math.max(0, x1);
        y1 = Math.max(0, y1);

        x1 = Math.min(x1, image.getWidth() - 1);
        y1 = Math.min(y1, image.getHeight() - 1);

        x2 = Math.min(x2, image.getWidth() - 1);
        y2 = Math.min(y2, image.getHeight() - 1);

        BufferedImage subImage = image.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        return colorDarknessRatio(subImage);
    }

    public static double colorDarknessRatio(BufferedImage image) {
        int[] colors = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        double ratioSum = 0;
        for (int rgb : colors) {
            ratioSum += colorDarknessRatio(rgb);
        }

        return ratioSum / colors.length;
    }
}
