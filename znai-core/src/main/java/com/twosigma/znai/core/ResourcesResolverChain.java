package com.twosigma.znai.core;

import com.twosigma.utils.ServiceLoaderUtils;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ResourcesResolverChain implements ResourcesResolver {
    private List<ResourcesResolver> resolvers = new ArrayList<>(ServiceLoaderUtils.load(ResourcesResolver.class));

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
        return resolver(path).fullPath(path);
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
                .orElseThrow(() -> new RuntimeException("can't find ResourceResolver to handle \"" + path + "\":\n" +
                    resolvers.stream().map(r -> renderResolverDetails(r, path)).collect(joining("\n"))));
    }

    private String renderResolverDetails(ResourcesResolver resolver, String path) {
        return resolver.getClass().getCanonicalName() + " resources not found:\n  " +
                resolver.listOfTriedLocations(path).stream().collect(joining("\n  "));
    }
}
