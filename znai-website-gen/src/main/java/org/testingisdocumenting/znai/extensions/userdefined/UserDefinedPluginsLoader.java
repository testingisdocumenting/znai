/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.extensions.userdefined;

import org.testingisdocumenting.znai.extensions.Plugins;
import org.testingisdocumenting.znai.resources.ResourcesResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDefinedPluginsLoader {
    private static final String PLUGINS_KEY = "plugins";

    private final ResourcesResolver resourcesResolver;
    private final List<UserDefinedPluginConfig> registered = new ArrayList<>();

    public UserDefinedPluginsLoader(ResourcesResolver resourcesResolver) {
        this.resourcesResolver = resourcesResolver;
    }

    @SuppressWarnings("unchecked")
    public void load(Map<String, ?> extensionsDefinition) {
        if (extensionsDefinition == null) {
            return;
        }

        Object raw = extensionsDefinition.get(PLUGINS_KEY);
        if (raw == null) {
            return;
        }

        if (!(raw instanceof List)) {
            throw new IllegalArgumentException("extensions <" + PLUGINS_KEY +
                    "> must be a list of plugin config file paths");
        }

        for (Object entry : (List<Object>) raw) {
            if (!(entry instanceof String)) {
                throw new IllegalArgumentException("extensions <" + PLUGINS_KEY +
                        "> must contain string paths, got: " + entry);
            }

            register(UserDefinedPluginConfig.load(resourcesResolver, (String) entry));
        }
    }

    public void unregister() {
        for (UserDefinedPluginConfig config : registered) {
            switch (config.getRole()) {
                case INCLUDE -> Plugins.removeUserIncludePlugin(config.getId());
                case FENCE -> Plugins.removeUserFencePlugin(config.getId());
            }
        }
        registered.clear();
    }

    private void register(UserDefinedPluginConfig config) {
        switch (config.getRole()) {
            case INCLUDE -> Plugins.registerUserPlugin(new UserDefinedIncludePlugin(config));
            case FENCE -> Plugins.registerUserPlugin(new UserDefinedFencePlugin(config));
        }

        registered.add(config);
    }
}
