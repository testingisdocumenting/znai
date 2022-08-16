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

package org.testingisdocumenting.znai.charts;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

abstract public class ChartIncludeBasePlugin implements IncludePlugin {
    protected static String HORIZONTAL_KEY = "horizontal";
    protected static String HEIGHT_KEY = "height";
    protected static String STACK_KEY = "stack";
    protected static String WIDE_KEY = "wide";
    protected static String LEGEND_KEY = "legend";
    protected static String TIME_KEY = "time";
    protected static String BREAKPOINT_KEY = "breakpoint";

    private Path fullPath;

    abstract protected String type();

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .add(HEIGHT_KEY, PluginParamType.NUMBER, "chart height", "500")
                .add(HORIZONTAL_KEY, PluginParamType.BOOLEAN, "horizontal bars", "true")
                .add(STACK_KEY, PluginParamType.BOOLEAN, "stack charts", "true")
                .add(WIDE_KEY, PluginParamType.BOOLEAN, "use all horizontal space", "true")
                .add(LEGEND_KEY, PluginParamType.BOOLEAN, "show legend", "true")
                .add(TIME_KEY, PluginParamType.BOOLEAN, "treat X axis data as time series", "true")
                .add(BREAKPOINT_KEY, PluginParamType.LIST_OR_SINGLE_STRING_OR_NUMBER, "list of X values to use for presentation mode breakpoint", "\"Thursday\" or [10, 54]");
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        fullPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        String csvContent = componentsRegistry.resourceResolver().textContent(fullPath);

        return ChartPluginResult.create(pluginParams, type(), csvContent);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
