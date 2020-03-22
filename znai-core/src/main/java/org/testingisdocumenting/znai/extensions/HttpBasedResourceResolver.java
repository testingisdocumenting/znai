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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.core.ResourcesResolver;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpBasedResourceResolver implements ResourcesResolver {
    private final List<String> httpBaseUrls;
    private final Map<String, Path> urlToCachedFile;
    private final Set<String> notFoundUrls;

    public HttpBasedResourceResolver() {
        httpBaseUrls = new ArrayList<>();
        urlToCachedFile = new ConcurrentHashMap<>();
        notFoundUrls = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    public void initialize(Stream<String> filteredLookupPaths) {
        httpBaseUrls.clear();
        httpBaseUrls.addAll(filteredLookupPaths.collect(Collectors.toList()));
    }

    @Override
    public boolean supportsLookupPath(String lookupPath) {
        return HttpResource.isHttpResource(lookupPath);
    }

    @Override
    public boolean canResolve(String path) {
        if (path == null) {
            return false;
        }

        if (notFoundUrls.contains(path)) {
            return false;
        }

        Path cachedFile = urlToCachedFile.get(path);
        if (cachedFile != null) {
            return true;
        }

        cachedFile = createLocalCache(path);
        if (cachedFile == null) {
            notFoundUrls.add(path);
            return false;
        }

        urlToCachedFile.put(path, cachedFile);
        return true;
    }

    @Override
    public List<String> listOfTriedLocations(String path) {
        return fullUrls(path).collect(Collectors.toList());
    }

    @Override
    public Path fullPath(String path) {
        Path cachedFile = urlToCachedFile.get(path);
        if (cachedFile == null) {
            throw new IllegalStateException("file should be cached. check canResolve method");
        }

        return cachedFile;
    }

    private Path createLocalCache(String path) {
        HttpResource httpResource = fullUrls(path)
                .map(HttpResource::new)
                .filter(HttpResource::exists)
                .findFirst()
                .orElse(null);

        if (httpResource == null) {
            return null;
        }

        Path cachedFile = createTempFile();
        cachedFile.toFile().deleteOnExit();

        FileUtils.writeTextContent(cachedFile, httpResource.getContent());

        return cachedFile;
    }

    private Path createTempFile() {
        try {
            return Files.createTempFile("httpResource", "cache");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path docRootRelativePath(Path path) {
        return null;
    }

    @Override
    public boolean isInsideDoc(Path path) {
        return false;
    }

    @Override
    public boolean isLocalFile(String path) {
        return false;
    }

    private Stream<String> fullUrls(String path) {
        return httpBaseUrls.stream()
                .map(baseUrl -> UrlUtils.concat(baseUrl, path));
    }
}
