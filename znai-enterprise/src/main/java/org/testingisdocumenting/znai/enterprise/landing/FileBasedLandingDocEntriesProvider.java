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

package org.testingisdocumenting.znai.enterprise.landing;

import org.testingisdocumenting.znai.structure.DocMeta;
import org.testingisdocumenting.znai.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBasedLandingDocEntriesProvider implements LandingDocEntriesProvider {
    public static final String META_FILE_NAME = "meta.json";
    private final Path storageRoot;
    private final Map<String, DocMeta> docMetaByDocId;

    public FileBasedLandingDocEntriesProvider(Path storageRoot) {
        this.storageRoot = storageRoot;
        this.docMetaByDocId = enumerateDocMetas();
    }

    @Override
    public Stream<LandingDocEntry> provide() {
        return docMetaByDocId.entrySet().stream()
                .map(entry -> new LandingDocEntry(
                        entry.getKey(),
                        entry.getValue().getTitle(),
                        "",
                        entry.getValue().getCategory(),
                        entry.getValue().getDescription()));
    }

    @Override
    public void onNewDocMeta(String docId, DocMeta docMeta) {
        docMetaByDocId.put(docId, docMeta);
    }

    private Map<String, DocMeta> enumerateDocMetas() {
        if (storageRoot == null || !Files.exists(storageRoot)) {
            return new HashMap<>();
        }

        try {
            return Files.list(storageRoot)
                    .filter(file -> Files.isDirectory(file))
                    .filter(file -> Files.exists(file.resolve(META_FILE_NAME)))
                    .map(file -> file.resolve(META_FILE_NAME))
                    .collect(Collectors.toMap(
                            fileMeta -> fileMeta.getParent().getFileName().toString(),
                            fileMeta -> new DocMeta(FileUtils.fileTextContent(fileMeta))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
