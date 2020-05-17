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

import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.Set;

public class ServerLifecycleListeners {
    private static final Set<ServerLifecycleListener> listeners =
            ServiceLoaderUtils.load(ServerLifecycleListener.class);

    public static void add(ServerLifecycleListener listener) {
        listeners.add(listener);
    }

    public static void remove(ServerLifecycleListener listener) {
        listeners.remove(listener);
    }

    public static void beforeStart(ZnaiServerConfig config) {
        listeners.forEach(l -> l.beforeStart(config));
    }
}
