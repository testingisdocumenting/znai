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

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.Set;

public class RoutesProviders {
    private static final Set<RoutesProvider> providers =
            ServiceLoaderUtils.load(RoutesProvider.class);

    public static void add(RoutesProvider provider) {
        providers.add(provider);
    }

    public static void remove(RoutesProvider provider) {
        providers.remove(provider);
    }

    public static void register(Vertx vertx, ZnaiServerConfig config, Router router) {
        providers.forEach(p -> p.register(vertx, config, router));
    }
}
