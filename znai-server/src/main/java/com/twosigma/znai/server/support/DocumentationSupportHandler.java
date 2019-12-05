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

package com.twosigma.znai.server.support;

import com.twosigma.znai.utils.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class DocumentationSupportHandler implements Handler<HttpServerRequest> {
    private final String docId;
    private final String actor;

    public DocumentationSupportHandler(String docId, String actor) {
        this.docId = docId;
        this.actor = actor;
    }

    @Override
    public void handle(HttpServerRequest request) {
        request.pause();
        HttpServerResponse response = request.response();
        SupportMeta supportMeta = OnSupportFinishedServerHandler.onFinished(docId, actor);
        response.putHeader("content-type", "application/json")
                .end(JsonUtils.serialize(supportMeta));
        request.resume();
    }
}
