/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.resources;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.utils.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ClassPathResourceResolver implements ResourcesResolver {
    private final ConcurrentHashMap<String, Path> fullPathByResourcePath = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Path, String> resourcePathByFullPath = new ConcurrentHashMap<>();

    @Override
    public void initialize(Stream<String> filteredLookupPaths) {
    }

    @Override
    public boolean supportsLookupPath(String lookupPath) {
        return false;
    }

    @Override
    public boolean canResolve(String resourcePath) {
        boolean canResolve = ResourceUtils.resourceStream(resourcePath) != null;
        if (canResolve) {
            Path path = Paths.get(resourcePath);
            fullPathByResourcePath.put(resourcePath, path);
            resourcePathByFullPath.put(path, resourcePath);
        }

        return canResolve;
    }

    @Override
    public List<String> listOfTriedLocations(String path) {
        return Collections.singletonList("<classpath>");
    }

    @Override
    public String textContent(String path) {
        return ResourceUtils.textContent(path);
    }

    @Override
    public BufferedImage imageContent(String path) {
        try {
            return ImageIO.read(ResourceUtils.resourceStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Can't load image " + path, e);
        }
    }

    @Override
    public String textContent(Path path) {
        String resourcePath = resourcePathByFullPath.get(path);
        return ResourceUtils.textContent(resourcePath);
    }

    @Override
    public Path fullPath(String path) {
        return fullPathByResourcePath.get(path);
    }

    @Override
    public Path docRootRelativePath(Path path) {
        throw new UnsupportedOperationException("unsupported operation: docRootRelativePath(path)");
    }

    @Override
    public boolean isInsideDoc(Path path) {
        return false;
    }

    @Override
    public boolean isLocalFile(String path) {
        return false;
    }

    @Override
    public AuxiliaryFile runtimeAuxiliaryFile(String origin) {
        throw new UnsupportedOperationException("unsupported operation: runtimeAuxiliaryFile(origin)");
    }
}
