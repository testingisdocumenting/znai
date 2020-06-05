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

package org.testingisdocumenting.znai.enterprise;

import org.testingisdocumenting.znai.structure.DocMeta;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.Set;

public class DocLifecycleListeners {
    private static final Set<DocLifecycleListener> listeners =
            ServiceLoaderUtils.load(DocLifecycleListener.class);

    public static void add(DocLifecycleListener listener) {
        listeners.add(listener);
    }

    public static void remove(DocLifecycleListener listener) {
        listeners.remove(listener);
    }

    public static void onDocUpdate(DocMeta docMeta) {
        listeners.forEach(l -> l.onDocUpdate(docMeta));
    }

    public static void onDocRemove(String docId) {
        listeners.forEach(l -> l.onDocRemove(docId));
    }
}
