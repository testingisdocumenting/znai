/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.doxygen.plugin;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.doxygen.Doxygen;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenDescription;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMembersList;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class DoxygenDocParamsIncludePlugin implements IncludePlugin {
    private DoxygenMembersList membersList;

    @Override
    public String id() {
        return "doxygen-doc-params";
    }

    @Override
    public IncludePlugin create() {
        return createDocParamsPlugin();
    }

    public static IncludePlugin createDocParamsPlugin() {
        return new DoxygenDocParamsIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        Doxygen doxygen = Doxygen.INSTANCE;

        String fullName = pluginParams.getFreeParam();

        membersList = DoxygenMemberListExtractor.extract(doxygen, componentsRegistry,
                pluginParams.getOpts(), false, fullName);

        if (membersList.isEmpty()) {
            DoxygenMemberListExtractor.throwIfMembersListIsEmpty(doxygen, componentsRegistry, fullName);
        }

        DoxygenDescription description = membersList.first().getDescription();
        String type = pluginParams.getOpts().get("type", "");

        ApiParameters apiParameters = "template".equals(type) ?
                description.getApiTemplateParameters():
                description.getApiParameters();

        if (apiParameters == null) {
            return PluginResult.empty();
        }

        Map<String, Object> props = pluginParams.getOpts().toMap();
        props.putAll(apiParameters.toMap());
        props.putAll(pluginParams.getOpts().toMap());
        return PluginResult.docElement("ApiParameters", props);
    }

    @Override
    public SearchText textForSearch() {
        ApiParameters apiParameters = membersList.first().getDescription().getApiParameters();
        if (apiParameters != null) {
            return SearchScore.HIGH.text(apiParameters.combinedTextForSearch());
        }

        return null;
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(Doxygen.INSTANCE.getIndexPath()));
    }
}
