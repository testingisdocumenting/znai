package com.twosigma.documentation.extensions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.twosigma.documentation.utils.ServiceUtils;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @author mykola
 */
public class IncludePlugins {
    private static Map<String, IncludePlugin> pluginsById = discover();

    public static IncludePlugin byId(String id) {
        final IncludePlugin includePlugin = pluginsById.get(id);
        if (includePlugin == null) {
            throw new RuntimeException(
                "can't find plugin with id '" + id + "'. full list\n: " + renderListOfPlugins(pluginsById.values()));
        }

        return includePlugin;
    }

    public static void reset(IncludeContext context) {
        pluginsById.values().forEach(p -> p.reset(context));
    }

    private static Map<String, IncludePlugin> discover() {
        final List<IncludePlugin> list = ServiceUtils.discover(IncludePlugin.class);

        final Map<String, IncludePlugin> byId = list.stream().collect(toMap(IncludePlugin::id, p -> p));
        if (byId.size() < list.size()) {
            throw new IllegalStateException("multiple plugins with the same id are detected. full list: \n" +
                renderListOfPlugins(list));
        }

        return byId;
        // TODO plugins as JavaScripts using Nashorn
    }

    private static String renderListOfPlugins(Collection<IncludePlugin> list) {
        return list.stream().map(p -> p.id() + ":" + p.getClass()).collect(joining("\n"));
    }
}
