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

package org.testingisdocumenting.znai.enterprise.upload;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.testingisdocumenting.znai.server.RoutesProvider;
import org.testingisdocumenting.znai.server.ZnaiServerConfig;

public class UploadDocumentationRouteProvider implements RoutesProvider {
    @Override
    public void register(Vertx vertx, ZnaiServerConfig config, Router router) {
        router.route("/upload/:docId").handler(ctx -> {
            MultiMap params = ctx.request().params();
            String docId = params.get("docId");
            String actor = params.get("actor");
            new DocumentationUploadVertxHandler(vertx, config, docId, actor).handle(ctx.request());
        });
    }
}
