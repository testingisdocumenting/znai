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

package com.twosigma.znai.server.remove;

import com.twosigma.znai.utils.ServiceLoaderUtils;

import java.util.Set;

public class OnRemoveFinishedServerHandlers {
    private static final Set<OnRemoveFinishedServerHandler> handlers =
            ServiceLoaderUtils.load(OnRemoveFinishedServerHandler.class);

    public static void onRemoveFinished(String docId, String actor) {
        handlers.forEach(h -> h.onRemoveFinished(docId, actor));
    }

    public static void add(OnRemoveFinishedServerHandler handler) {
        handlers.add(handler);
    }
}
