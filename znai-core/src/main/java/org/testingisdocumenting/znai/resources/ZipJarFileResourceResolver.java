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

package org.testingisdocumenting.znai.resources;

import org.apache.commons.io.FileUtils;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.core.Log;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZipJarFileResourceResolver implements ResourcesResolver {
    private final Log log;
    private final Path docRootPath;

    private Path allUnarchivedRoot;

    private final List<Path> resolvedArchivesPaths = new ArrayList<>();

    public ZipJarFileResourceResolver(Log log, Path docRootPath) {
        this.log = log;
        this.docRootPath = docRootPath;
    }

    @Override
    public void initialize(Stream<String> filteredLookupPaths) {
        allUnarchivedRoot = createTempDirectory();

        resolvedArchivesPaths.clear();
        filteredLookupPaths
                .map(this::archiveAbsolutePath)
                .forEach(resolvedArchivesPaths::add);

        if (!resolvedArchivesPaths.isEmpty()) {
            log.phase("un-archiving lookup files");
        }

        resolvedArchivesPaths.forEach(this::unArchive);
    }

    @Override
    public boolean supportsLookupPath(String lookupPath) {
        Path path = docRootPath.resolve(lookupPath);
        return Files.exists(path) && (lookupPath.endsWith(".zip") || lookupPath.endsWith(".jar"));
    }

    @Override
    public boolean canResolve(String path) {
        return Files.exists(allUnarchivedRoot.resolve(path));
    }

    @Override
    public List<String> listOfTriedLocations(String path) {
        return resolvedArchivesPaths.stream()
                .map(archivePath -> archivePath + "/" + path)
                .collect(Collectors.toList());
    }

    @Override
    public Path fullPath(String path) {
        return allUnarchivedRoot.resolve(path);
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

    private void unArchive(Path archivePath) {
        try {
            log.info(Color.BLUE, "un-archiving ", Color.PURPLE, archivePath, Color.BLUE, " to ", Color.PURPLE, allUnarchivedRoot);
            unarchive(archivePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unarchive(Path archivePath) throws IOException {
        try (FileSystem archiveFs = FileSystems.newFileSystem(archivePath, null)) {
            Iterable<Path> rootDirectories = archiveFs.getRootDirectories();
            for (Path root : rootDirectories) {
                Files.walk(root).forEach(path -> {
                    try {
                        if (Files.isRegularFile(path)) {
                            Path dest = allUnarchivedRoot.resolve(
                                    // remove leading /
                                    path.toString().substring(1));
                            Files.createDirectories(dest.getParent());
                            Files.copy(path, dest);
                        }
                    } catch (IOException e) {
                        log.warn("can't unarchive file " + path + " from " + archivePath);
                    }
                });
            }
        }
    }

    private Path archiveAbsolutePath(String archiveLookupPath) {
        Path archiveRelativeOrAbsolutePath = Paths.get(archiveLookupPath);
        if (archiveRelativeOrAbsolutePath.isAbsolute()) {
            return archiveRelativeOrAbsolutePath;
        }

        return docRootPath.resolve(archiveLookupPath);
    }

    private Path createTempDirectory()  {
        try {
            final Path tempDirectory = Files.createTempDirectory("znai-archive-resource-resolver");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> FileUtils.deleteQuietly(tempDirectory.toFile())));

            return tempDirectory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
