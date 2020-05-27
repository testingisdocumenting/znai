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

package org.testingisdocumenting.znai.enterprise.storage;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.website.DocumentationFileBasedTimestamp;
import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationProgress;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.testingisdocumenting.znai.fs.FsUtils.*;

public class FileBasedDocumentationStorage implements DocumentationStorage {
    private final Path storageRoot;
    private final Path docsRoot;

    public FileBasedDocumentationStorage(Path storageRoot, Path docsRootPath) {
        this.storageRoot = storageRoot;
        this.docsRoot = docsRootPath;
    }

    @Override
    public void store(String docId, String version, Path generatedDocumentation) {
        Path dest = storageRoot.resolve(docId).resolve(version);
        deleteDirectory(dest);
        copyDirectory(generatedDocumentation, dest);

        ConsoleOutputs.out("stored ", Color.WHITE, docId, Color.BLUE, " as ", Color.PURPLE, dest);
    }

    @Override
    public void prepare(String docId, String version,
                        DocumentationPreparationProgress progress) {
        progress.reportProgress("Checking documentation", Collections.emptyMap(), 10);
        Path src = storageRoot.resolve(docId).resolve(version);

        Path tempDestRoot = creteTempDir();
        Path tempDest = tempDestRoot.resolve(docId).resolve(version);
        Path finalDest = docsRoot.resolve(docId).resolve(version);

        deleteDirectory(tempDest);
        copyDirectory(src, tempDest);
        progress.reportProgress("Moved documentation to a temporary location", Collections.emptyMap(), 40);

        progress.reportProgress("Moving documentation to a permanent location", Collections.emptyMap(), 80);
        deleteDirectory(finalDest);

        moveDirectory(tempDest, finalDest);
        deleteDirectory(tempDestRoot);

        progress.reportProgress("Moved documentation to a permanent location", Collections.emptyMap(), 100);
    }

    private Path creteTempDir() {
        try {
            return Files.createTempDirectory(docsRoot, "temp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long lastUpdateTime(String docId, String version) {
        return DocumentationFileBasedTimestamp.read(storageRoot.resolve(docId).resolve(version));
    }
}
