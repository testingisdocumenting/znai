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

package org.testingisdocumenting.znai.server.auth;

import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorizationHandlers {
    private static final Set<AuthorizationHandler> handlers =
            ServiceLoaderUtils.load(AuthorizationHandler.class);

    public static boolean isAuthorized(String userId, String docId) {
        if (handlers.isEmpty()) {
            return true;
        }

        return handlers.stream()
                .anyMatch(h -> h.isAuthorized(userId, docId));
    }

    public static AuthorizationRequestLink authorizationRequestLink() {
        return handlers.stream()
                .map(AuthorizationHandler::authorizationRequestLink)
                .filter(link -> !link.isEmpty())
                .findFirst()
                .orElse(new AuthorizationRequestLink("", ""));
    }

    public static List<String> allowedGroups(String docId) {
        return handlers.stream()
                .flatMap(h -> h.allowedGroups(docId).stream())
                .collect(Collectors.toList());
    }

    public static void add(AuthorizationHandler handler) {
        handlers.add(handler);
    }

    public static void remove(AuthorizationHandler handler) {
        handlers.remove(handler);
    }
}
