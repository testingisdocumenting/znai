package com.twosigma.znai.extensions;

import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.UrlUtils;

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
