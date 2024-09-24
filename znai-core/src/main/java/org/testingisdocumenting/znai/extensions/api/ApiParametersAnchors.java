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

package org.testingisdocumenting.znai.extensions.api;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.structure.AnchorIds;
import org.testingisdocumenting.znai.structure.DocStructure;

import java.nio.file.Path;
import java.util.Collections;

class ApiParametersAnchors {
    private ApiParametersAnchors() {
    }

    static void registerLocalAnchors(ComponentsRegistry componentsRegistry,
                                     Path markupPath,
                                     ApiParameters apiParameters) {
        DocStructure docStructure = componentsRegistry.docStructure();
        apiParameters.collectAllAnchors().forEach(anchorId -> docStructure.registerLocalAnchors(markupPath,
                new AnchorIds(anchorId, Collections.emptyList())));
    }

    static String anchorIdFromNameAndPrefix(String prefix, String name) {
        String sanitizeAnchorId = sanitizeAnchorId(name);

        if (sanitizeAnchorId.isEmpty()) {
            return prefix;
        }

        return prefix + (prefix.isEmpty() ? "" : "_") + sanitizeAnchorId;
    }

    static String sanitizeAnchorId(String anchorId) {
        return anchorId.replace('.', '_').replaceAll("[\\\\*<> ]", "");
    }
}
