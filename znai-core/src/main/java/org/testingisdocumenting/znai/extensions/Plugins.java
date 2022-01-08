/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class Plugins {
    private static final Map<String, Plugin> includePluginsById = discoverIncludePlugins();
    private static final Map<String, Plugin> fencePluginsById = discoverFencePlugins();
    private static final Map<String, Plugin> inlineCodePluginsById = discoverInlinedCodePlugins();

    private static final PluginsTracker pluginsTracker = new PluginsTracker();

    public static Map<String, ?> buildStatsMap() {
        return pluginsTracker.buildStatsMap();
    }
    
    public static IncludePlugin includePluginById(String id) {
        pluginsTracker.includePlugins.increment(id);
        return new TrackingIncludePlugin((IncludePlugin) pluginById(includePluginsById, id),
                pluginsTracker.includePlugins.createParamsTracker(id));
    }

    public static boolean hasFencePlugin(String id) {
        return fencePluginsById.containsKey(id);
    }

    public static FencePlugin fencePluginById(String id) {
        pluginsTracker.fencePlugins.increment(id);
        return new TrackingFencePlugin((FencePlugin) pluginById(fencePluginsById, id),
                pluginsTracker.fencePlugins.createParamsTracker(id));
    }

    public static boolean hasInlinedCodePlugin(String id) {
        return inlineCodePluginsById.containsKey(id);
    }

    public static InlinedCodePlugin inlinedCodePluginById(String id) {
        pluginsTracker.inlineCodePlugins.increment(id);
        return new TrackingInlineCodePlugin((InlinedCodePlugin) pluginById(inlineCodePluginsById, id),
                pluginsTracker.inlineCodePlugins.createParamsTracker(id));
    }

    private static Plugin pluginById(Map<String, Plugin> plugins, String id) {
        final Plugin plugin = plugins.get(id);
        if (plugin == null) {
            throw new RuntimeException(
                "can't find plugin with id '" + id + "'. full list\n: " + renderListOfPlugins(plugins.values()));
        }

        return plugin.create();
    }

    private static Map<String, Plugin> discoverIncludePlugins() {
        return discoverPlugins(IncludePlugin.class);
    }

    private static Map<String, Plugin> discoverFencePlugins() {
        return discoverPlugins(FencePlugin.class);
    }

    private static Map<String, Plugin> discoverInlinedCodePlugins() {
        return discoverPlugins(InlinedCodePlugin.class);
    }

    private static <E extends Plugin> Map<String, Plugin> discoverPlugins(Class<E> pluginType) {
        final Set<E> list = ServiceLoaderUtils.load(pluginType);

        final Map<String, Plugin> byId = list.stream().collect(toMap(Plugin::id, p -> p));
        if (byId.size() < list.size()) {
            throw new IllegalStateException("multiple plugins with the same id are detected. full list: \n" +
                renderListOfPlugins(list));
        }

        return byId;
    }

    private static <E extends Plugin> String renderListOfPlugins(Collection<E> list) {
        return list.stream().map(p -> p.id() + ":" + p.getClass()).collect(joining("\n"));
    }
}
