/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.server.preview;

import org.testingisdocumenting.znai.console.ConsoleOutput;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.console.ansi.FontStyle;
import org.testingisdocumenting.znai.server.sockets.JsonWebSocketHandler;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PreviewUpdatePathWebSocketHandler extends JsonWebSocketHandler implements ConsoleOutput {
    public PreviewUpdatePathWebSocketHandler(Consumer<Path> onPathReceived) {
        super("preview path change", "/_preview-update", (message) -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> deserialize = (Map<String, Object>) JsonUtils.deserialize(message);
            Path newPath = Paths.get(deserialize.get("path").toString());

            onPathReceived.accept(newPath);
        });
    }

    @Override
    public void out(Object... styleOrValues) {
        send("", convertToJson("out", styleOrValues));
    }

    @Override
    public void err(Object... styleOrValues) {
        send("", convertToJson("err", styleOrValues));
    }

    private Map<String, Object> convertToJson(String type, Object... styleOrValue) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", type);
        result.put("parts", Arrays.stream(styleOrValue).map(this::convertPartToJson).toList());
        return result;
    }

    private Map<String, String> convertPartToJson(Object styleOrValue) {
        if (styleOrValue == null) {
            return typeAndValue("text", "null");
        } else if (styleOrValue instanceof Color color) {
            return typeAndValue("color", color.name());
        } else if (styleOrValue instanceof FontStyle fontStyle) {
            return typeAndValue("font", fontStyle.name());
        }

        return typeAndValue("text", styleOrValue.toString());
    }

    private Map<String, String> typeAndValue(String type, String value) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("type", type);
        result.put("value", value);
        return result;
    }

    @Override
    public void onConnect(String uri) {
    }
}
