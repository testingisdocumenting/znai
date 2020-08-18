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

package org.testingisdocumenting.znai.server;

import io.vertx.ext.web.RoutingContext;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.server.auth.BasicInjectedAuthentication;

public class AuthorizationHeaderBasedAuthenticationHandler implements AuthenticationHandler {
    @Override
    public String authenticate(RoutingContext ctx, String docId) {
        String authorization = ctx.request().headers().get("Authorization");
        ConsoleOutputs.out(ctx.request().uri() + ", received authorization header for <" + docId + ">: " + authorization);

        String userId = BasicInjectedAuthentication.extractUserId(authorization);
        if (userId.isEmpty()) {
            ConsoleOutputs.out("no user id is extracted: no authorization is being performed for <" + docId + ">");
        }

        return userId;
    }
}
