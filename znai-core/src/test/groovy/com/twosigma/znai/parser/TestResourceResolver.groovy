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

package com.twosigma.znai.parser

import com.twosigma.znai.core.ResourcesResolver
import com.twosigma.utils.ResourceUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class TestResourceResolver implements ResourcesResolver {
    private Path root

    TestResourceResolver(Path root) {
        this.root = root.toAbsolutePath()
    }

    @Override
    void initialize(Stream<String> filteredLookupPaths) {
    }

    @Override
    boolean supportsLookupPath(String lookupPath) {
        return true
    }

    @Override
    boolean canResolve(String path) {
        return ResourceUtils.resourceStream(path) != null
    }

    @Override
    List<String> listOfTriedLocations(String path) {
        return []
    }

    @Override
    String textContent(String path) {
        return ResourceUtils.textContent(path)
    }

    @Override
    String textContent(Path path) {
        return ResourceUtils.textContent(path.fileName.toString())
    }

    @Override
    BufferedImage imageContent(String path) {
        return ImageIO.read(ResourceUtils.requiredResourceStream(path))
    }

    @Override
    Path fullPath(String path) {
        return root ? root.resolve(path) : Paths.get(path).toAbsolutePath()
    }

    @Override
    Path docRootRelativePath(Path path) {
        return path.fileName
    }

    @Override
    boolean isInsideDoc(Path path) {
        return path.toAbsolutePath().startsWith(root)
    }

    @Override
    boolean isLocalFile(String path) {
        return ResourceUtils.resourceStream(path) != null
    }
}
