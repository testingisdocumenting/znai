package com.twosigma.documentation.extensions;

import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.inlinedcode.InlinedCodePlugin;
import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @author mykola
 */
public class Plugins {
    private static Map<String, IncludePlugin> includePluginsById = discoverIncludePlugins();
    private static Map<String, FencePlugin> fencePluginsById = discoverFencePlugins();
    private static Map<String, InlinedCodePlugin> inlineCodePluginsById = discoverInlinedCodePlugins();

    public static IncludePlugin includePluginById(String id) {
        return pluginById(includePluginsById, id);
    }

    public static boolean hasFencePlugin(String id) {
        return fencePluginsById.containsKey(id);
    }

    public static FencePlugin fencePluginById(String id) {
        return pluginById(fencePluginsById, id);
    }

    public static boolean hasInlinedCodePlugin(String id) {
        return inlineCodePluginsById.containsKey(id);
    }

    public static InlinedCodePlugin inlinedCodePluginById(String id) {
        return pluginById(inlineCodePluginsById, id);
    }

    private static <E extends Plugin> E pluginById(Map<String, E> plugins, String id) {
        final E plugin = plugins.get(id);
        if (plugin == null) {
            throw new RuntimeException(
                "can't find plugin with id '" + id + "'. full list\n: " + renderListOfPlugins(plugins.values()));
        }

        return plugin;
    }

    public static void reset(IncludeContext context) {
        includePluginsById.values().forEach(p -> p.reset(context));
    }

    private static Map<String, IncludePlugin> discoverIncludePlugins() {
        return discoverPlugins(IncludePlugin.class);
    }

    private static Map<String, FencePlugin> discoverFencePlugins() {
        return discoverPlugins(FencePlugin.class);
    }

    private static Map<String, InlinedCodePlugin> discoverInlinedCodePlugins() {
        return discoverPlugins(InlinedCodePlugin.class);
    }

    private static <E extends Plugin> Map<String, E> discoverPlugins(Class<E> pluginType) {
        final Set<E> list = ServiceLoaderUtils.load(pluginType);

        final Map<String, E> byId = list.stream().collect(toMap(Plugin::id, p -> p));
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
