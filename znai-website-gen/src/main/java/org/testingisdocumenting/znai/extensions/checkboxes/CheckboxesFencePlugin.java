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

package org.testingisdocumenting.znai.extensions.checkboxes;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.NameUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class CheckboxesFencePlugin implements FencePlugin {
    private static final int MAX_ID_LENGTH = 48;

    private MarkupParserResult contentParseResult;

    @Override
    public String id() {
        return "checkboxes";
    }

    @Override
    public FencePlugin create() {
        return new CheckboxesFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        contentParseResult = componentsRegistry.defaultParser().parse(markupPath, content);

        List<Map<String, Object>> checkboxItems = buildItems();
        if (checkboxItems.isEmpty()) {
            throw new IllegalArgumentException("no bullet points found inside checkboxes fence block");
        }

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("blockId", blockIdFromContent(content));
        props.put("checkboxItems", checkboxItems);

        return PluginResult.docElement("Checkboxes", props);
    }

    /**
     * block identity is derived from the content, so blocks with the same item texts don't share
     * persisted checked state, and editing a checklist resets its state
     */
    private static String blockIdFromContent(String content) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(content.trim().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash, 0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return contentParseResult.auxiliaryFiles().stream();
    }

    @Override
    public List<SearchText> textForSearch() {
        return List.of(SearchScore.STANDARD.text(contentParseResult.getAllText()));
    }

    private List<Map<String, Object>> buildItems() {
        List<Map<String, Object>> items = new ArrayList<>();
        Set<String> usedIds = new HashSet<>();

        for (DocElement element : contentParseResult.docElement().getContent()) {
            if (!element.getType().equals(DocElementType.BULLET_LIST)) {
                throw new IllegalArgumentException("only bullet points are supported inside checkboxes fence block, " +
                        "found: " + element.getType());
            }

            for (DocElement listItem : element.getContent()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", buildItemId(listItem, usedIds));
                item.put("content", listItem.contentToListOfMaps());

                items.add(item);
            }
        }

        return items;
    }

    private String buildItemId(DocElement listItem, Set<String> usedIds) {
        StringBuilder text = new StringBuilder();
        collectText(listItem, text);

        String id = NameUtils.idFromTitle(text.toString().trim());
        if (id.length() > MAX_ID_LENGTH) {
            id = id.substring(0, MAX_ID_LENGTH);
        }

        if (id.isEmpty()) {
            id = "item";
        }

        String result = id;
        int suffix = 2;
        while (!usedIds.add(result)) {
            result = id + "-" + suffix++;
        }

        return result;
    }

    private void collectText(DocElement element, StringBuilder result) {
        Object text = element.getProp("text");
        if (text == null) {
            text = element.getProp("code");
        }

        if (text != null) {
            if (!result.isEmpty()) {
                result.append(' ');
            }
            result.append(text);
        }

        element.getContent().forEach(child -> collectText(child, result));
    }
}
