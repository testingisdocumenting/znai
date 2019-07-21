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

package com.twosigma.znai.server.docpreparation;

import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;

public class DocumentationPreparationHandlers {
    private static Set<DocumentationPreparationHandler> handlers =
            ServiceLoaderUtils.load(DocumentationPreparationHandler.class);

    public static void prepare(String docId, DocumentationPreparationProgress preparationProgress) {
        DocumentationPreparationHandler handler = handlers.stream()
                .filter(h -> h.handles(docId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "can't find handler for documentation preparation of documentation: " + docId));

        handler.prepare(docId, preparationProgress);
    }

    public static boolean isReady(String docId) {
        return handlers.isEmpty() || handlers.stream().anyMatch(h -> h.isReady(docId));
    }

    public static void add(DocumentationPreparationHandler handler) {
        handlers.add(handler);
    }
}
