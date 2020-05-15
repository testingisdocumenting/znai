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

package org.testingisdocumenting.znai.enterprise.docpreparation;

import io.vertx.core.impl.ConcurrentHashSet;
import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationHandler;
import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationProgress;

import java.util.*;

public class DocumentationPreparationTestHandler implements DocumentationPreparationHandler {
    private static final List<String> DUMMY_DOC_IDS = Arrays.asList("preview", "hello", "world");

    private final Set<String> isReadyById = new ConcurrentHashSet<>();

    @Override
    public boolean handles(String docId) {
        return DUMMY_DOC_IDS.contains(docId);
    }

    @Override
    public boolean isReady(String docId) {
        return isReadyById.contains(docId);
    }

    @Override
    public void prepare(String docId, DocumentationPreparationProgress preparationProgress) {
        Map<String, String> keyValues = new LinkedHashMap<>();

        try {
            sleep(1000);
            preparationProgress.reportProgress(docId + ": hello", keyValues, 15);

            sleep(1000);
            keyValues.put("code base", "test_codebase");
            preparationProgress.reportProgress(docId + ": world", keyValues,45);

            sleep(2000);
            keyValues.put("branch", "branch name");
            preparationProgress.reportProgress(docId + ": some progress", keyValues, 75);

            sleep(1000);
            isReadyById.add(docId);
            preparationProgress.reportProgress(docId + ": another progress", keyValues, 100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
        System.out.println(Thread.currentThread().getName());
    }
}
