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

package org.testingisdocumenting.znai.enterprise.remove;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import org.testingisdocumenting.znai.server.ZnaiServerConfig;

public class DocumentationRemoveVertxHandler implements Handler<HttpServerRequest> {
    private final ZnaiServerConfig config;
    private final String docId;
    private final String actor;

    public DocumentationRemoveVertxHandler(ZnaiServerConfig config, String docId, String actor) {
        this.config = config;
        this.docId = docId;
        this.actor = actor;
    }

    @Override
    public void handle(HttpServerRequest req) {
        req.pause();
        req.response().end();
        OnRemoveFinishedServerHandlers.onRemoveFinished(config, docId, actor);
        req.resume();
    }
}
