/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.resources;

import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourcesResolverChain implements ResourcesResolver {
    private final Map<String, Path> outsideDocRequestedResources = new HashMap<>();
    private final List<ResourcesResolver> resolvers = new ArrayList<>(ServiceLoaderUtils.load(ResourcesResolver.class));

    public void addResolver(ResourcesResolver resourcesResolver) {
        resolvers.add(resourcesResolver);
    }

    @Override
    public void initialize(Stream<String> filteredLookupPaths) {
        List<String> lookupPaths = filteredLookupPaths.collect(Collectors.toList());
        resolvers.forEach(r -> initializeWithFilteredPaths(r, lookupPaths.stream()));
    }

    @Override
    public boolean supportsLookupPath(String lookupPath) {
        return true;
    }

    @Override
    public boolean canResolve(String path) {
        return resolvers.stream()
                .map(r -> r.canResolve(path))
                .filter(can -> can)
                .findFirst().orElse(false);
    }

    @Override
    public List<String> listOfTriedLocations(String path) {
        return resolvers.stream()
                .flatMap(r -> r.listOfTriedLocations(path).stream())
                .collect(Collectors.toList());
    }

    @Override
    public String textContent(String path) {
        return resolver(path).textContent(path);
    }

    @Override
    public BufferedImage imageContent(String path) {
        return resolver(path).imageContent(path);
    }

    @Override
    public Path fullPath(String path) {
        Path result = resolver(path).fullPath(path);

        if (!isInsideDoc(result)) {
            outsideDocRequestedResources.put(path, result);
        }

        return result;
    }

    @Override
    public Path docRootRelativePath(Path path) {
        return resolver(path.toString()).docRootRelativePath(path);
    }

    @Override
    public boolean isInsideDoc(Path path) {
        return resolver(path.toString()).isInsideDoc(path);
    }

    @Override
    public boolean isLocalFile(String path) {
        return resolver(path).isLocalFile(path);
    }

    public Map<String, Path> getOutsideDocRequestedResources() {
        return outsideDocRequestedResources;
    }

    private void initializeWithFilteredPaths(ResourcesResolver r, Stream<String> filteredLookupPaths) {
        r.initialize(filteredLookupPaths
                .filter(r::supportsLookupPath)
                .collect(Collectors.toList())
                .stream());
    }

    private ResourcesResolver resolver(String path) {
        return resolvers.stream()
                .filter(r -> r.canResolve(path))
                .findFirst()
                .orElseThrow(() -> new UnresolvedResourceException(resolvers.stream(), path));
    }
}
