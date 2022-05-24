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

import org.testingisdocumenting.znai.structure.TableOfContents;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultipleLocalLocationsResourceResolver implements ResourcesResolver {
    private final TableOfContents toc;
    private final Path docRootPath;
    private final List<Path> lookupPaths;
    private final ThreadLocal<Path> currentFilePath;

    public MultipleLocalLocationsResourceResolver(TableOfContents toc, Path docRootPath) {
        this.toc = toc;
        this.docRootPath = docRootPath;
        this.lookupPaths = new ArrayList<>();
        this.currentFilePath = new ThreadLocal<>();
    }

    @Override
    public void initialize(Stream<String> filteredLookupPaths) {
        lookupPaths.clear();
        lookupPaths.addAll(filteredLookupPaths
                .map(docRootPath::resolve)
                .collect(Collectors.toList()));
    }

    @Override
    public boolean supportsLookupPath(String lookupPath) {
        Path path = docRootPath.resolve(lookupPath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    @Override
    public boolean canResolve(String path) {
        return isLocalFile(path);
    }

    @Override
    public List<String> listOfTriedLocations(String path) {
        return allLocationsStream(path).map(Path::toString).collect(Collectors.toList());
    }

    @Override
    public Path fullPath(String path) {
        return allLocationsStream(path).filter(Files::exists).findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "either file disappeared or canResolve implementation needs to be checked."));
    }

    @Override
    public Path docRootRelativePath(Path path) {
        return docRootPath.relativize(path);
    }

    @Override
    public boolean isInsideDoc(Path path) {
        return path.toAbsolutePath().startsWith(docRootPath);
    }

    @Override
    public boolean isLocalFile(String path) {
        return allLocationsStream(path).anyMatch(Files::exists);
    }

    private Stream<Path> allLocationsStream(String path) {
        Path original = Paths.get(path);

        Stream<Path> relativeToCurrent = currentFilePath.get() == null ? Stream.empty() :
                Stream.of(currentFilePath.get().getParent().resolve(path));

        Stream<Path> absoluteLocation = original.isAbsolute() ? Stream.of(original) : Stream.empty();
        Stream<Path> lookedUpInLocations = lookupPaths.stream().map(p -> p.resolve(path).normalize());

        return Stream.concat(relativeToCurrent, Stream.concat(absoluteLocation, lookedUpInLocations));
    }

    public void setCurrentFilePath(Path currentFilePath) {
        this.currentFilePath.set(currentFilePath);
    }
}
