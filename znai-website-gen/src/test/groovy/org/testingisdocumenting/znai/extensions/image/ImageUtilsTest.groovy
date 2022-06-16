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

package org.testingisdocumenting.znai.extensions.image

import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.image.BufferedImage

class ImageUtilsTest {
    def darkImageWithSpot = image("image-darkness-ratio.png")
    @Test
    void "darkness coefficient single pixel"() {
        ImageUtils.colorDarknessRatio(new Color(255, 250, 0).getRGB()).shouldBe < 0.5
        ImageUtils.colorDarknessRatio(new Color(20, 20, 0).getRGB()).shouldBe > 0.5

        ImageUtils.colorDarknessRatio(new Color(200, 200, 200).getRGB()).shouldBe < 0.5
    }

    @Test
    void "darkness coefficient image"() {
        ImageUtils.colorDarknessRatio(image("dummy.png")).shouldBe < 0.5
        ImageUtils.colorDarknessRatio(darkImageWithSpot).shouldBe > 0.5
    }

    @Test
    void "darkness coefficient sub image"() {
        ImageUtils.colorDarknessRatio(darkImageWithSpot, 40, 40, 80, 80).shouldBe < 0.5
    }

    @Test
    void "darkness coefficient with sample size"() {
        ImageUtils.colorDarknessRatio(darkImageWithSpot, 50, 50, 5).shouldBe < 0.5
    }

    @Test
    void "darkness coefficient sub image crop"() {
        ImageUtils.colorDarknessRatio(darkImageWithSpot, -5, -5, 2380, 2480).shouldBe > 0.5
    }

    private static BufferedImage image(String resourcePath) {
        return ImageIO.read(ResourceUtils.resourceStream(resourcePath))
    }
}
