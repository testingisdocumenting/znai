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

import org.testingisdocumenting.znai.enterprise.DocLifecycleListener;
import org.testingisdocumenting.znai.structure.DocMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.documentationStorage;

public class StorageBasedLandingDocEntriesProvider implements LandingDocEntriesProvider, DocLifecycleListener {
    private final Map<String, DocMeta> docMetaByDocId;

    public StorageBasedLandingDocEntriesProvider() {
        this.docMetaByDocId = enumerateDocMetas();
    }

    @Override
    public Stream<LandingDocEntry> provide() {
        return docMetaByDocId.entrySet().stream()
                .filter(entry -> entry.getValue().isDisplayOnLanding())
                .map(entry -> new LandingDocEntry(
                        entry.getKey(),
                        entry.getValue().getTitle(),
                        "",
                        entry.getValue().getCategory(),
                        entry.getValue().getDescription()));
    }

    private Map<String, DocMeta> enumerateDocMetas() {
        if (documentationStorage() == null) {
            return new HashMap<>();
        }

        return documentationStorage().list()
                .stream()
                .collect(Collectors.toMap(DocMeta::getId, Function.identity()));
    }

    @Override
    public void onDocUpdate(DocMeta docMeta) {
        docMetaByDocId.put(docMeta.getId(), docMeta);
    }

    @Override
    public void onDocRemove(String docId) {
        docMetaByDocId.remove(docId);
    }
}
