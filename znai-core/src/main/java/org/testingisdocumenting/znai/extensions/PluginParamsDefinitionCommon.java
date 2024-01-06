/*
 * Copyright 2022 znai maintainers
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

import org.testingisdocumenting.znai.extensions.file.AnchorFeature;

public class PluginParamsDefinitionCommon {
    public static final String TITLE_KEY = "title";

    public static final PluginParamsDefinition title = new PluginParamsDefinition()
            .add(TITLE_KEY, PluginParamType.STRING, "title to use for the container", "\"my title\"");

    public static final PluginParamsDefinition collapsible = new PluginParamsDefinition()
            .add("collapsed", PluginParamType.BOOLEAN,
                    "is collapsed by default", "true");

    public static final PluginParamsDefinition noGap = new PluginParamsDefinition()
            .add("noGap", PluginParamType.BOOLEAN,
                    "remove margins when multiple snippets in a row", "true");

    public static final PluginParamsDefinition noGapBorder = new PluginParamsDefinition()
            .add("noGapBorder", PluginParamType.BOOLEAN,
                    "add border line between containers", "true");

    public static final PluginParamsDefinition snippetReadMore = new PluginParamsDefinition()
            .add("readMore", PluginParamType.BOOLEAN,
                    "hides snippet behind \"read more\" button", "true")
            .add("readMoreVisibleLines", PluginParamType.NUMBER,
                    "number of lines to display when readMore is true", "10");

    public static final PluginParamsDefinition container = new PluginParamsDefinition()
            .add(title)
            .add(collapsible)
            .add(noGap)
            .add(noGapBorder)
            .add(AnchorFeature.paramsDefinition);

    public static final PluginParamsDefinition snippetWide = new PluginParamsDefinition()
            .add("wide", PluginParamType.BOOLEAN,
                    "force snippet to take all the available horizontal space", "true");

    public static final PluginParamsDefinition snippetRender = new PluginParamsDefinition()
            .add(snippetWide)
            .add("wrap", PluginParamType.BOOLEAN,
                    "force snippet soft wrapping", "true")
            .add(snippetReadMore)
            .add(container)
            .add("commentsType", PluginParamType.STRING,
                    "change way code comments are displayed: <inline> - use bullet points, <remove> - hide comments", "\"inline\"")
            .add("spoiler", PluginParamType.BOOLEAN,
                    "hide bullet points comments (commentsType: \"inline\") behind spoiler", "\"inline\"");
}
