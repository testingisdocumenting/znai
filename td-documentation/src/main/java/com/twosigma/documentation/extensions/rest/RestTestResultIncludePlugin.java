package com.twosigma.documentation.extensions.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.ReactComponent;
import com.twosigma.documentation.extensions.include.IncludePluginResult;

/**
 * @author mykola
 */
public class RestTestResultIncludePlugin implements IncludePlugin {
    private int nextCallNumber;

    @Override
    public String id() {
        return "rest-test-result";
    }

    @Override
    public void reset(final IncludeContext context) {
        nextCallNumber = 1;
    }

    @Override
    public IncludePluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, final IncludeParams includeParams) {
        final Map<String, Object> props = new LinkedHashMap<>();

        final Path pathToJson = Paths.get(includeParams.getFreeParam() + ".groovy-" + nextCallNumber + ".json");
        nextCallNumber++;

        final MapOrList mapOrList = createGson().fromJson(content(pathToJson), MapOrList.class);

        props.put("data", mapOrList.list != null ? mapOrList.list : mapOrList.map);
        return IncludePluginResult.reactComponent("RestTestOutput", props);
    }

    private String content(final Path pathToJson) {
        try {
            return Files.readAllLines(pathToJson).stream().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String textForSearch() {
        return "";
    }

    private static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(MapOrList.class, new MapOrListDeserializer()).create();
    }

    private static class MapOrList {
        private Map map;
        private List list;
    }

    // TODO extract

    private static class MapOrListDeserializer implements JsonDeserializer<MapOrList> {
        @Override
        public MapOrList deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws
            JsonParseException {
            final MapOrList result = new MapOrList();
            if (jsonElement.isJsonArray()) {
                result.list = jsonDeserializationContext.deserialize(jsonElement, List.class);
            } else {
                result.map = jsonDeserializationContext.deserialize(jsonElement, Map.class);
            }

            return result;
        }
    }
}
