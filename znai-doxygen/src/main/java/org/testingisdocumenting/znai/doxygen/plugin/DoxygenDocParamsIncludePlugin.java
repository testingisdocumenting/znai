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
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMember;
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
    private DoxygenMember member;

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
        member = doxygen.getCachedOrFindAndParseMember(componentsRegistry,
                fullName);

        if (member == null) {
            throw new RuntimeException("can't find member: " + fullName + ", available names:\n" +
                    doxygen.buildIndexOrGetCached(componentsRegistry).renderAvailableMemberNames());
        }

        DoxygenDescription description = member.getDescription();

        Map<String, Object> props = pluginParams.getOpts().toMap();
        ApiParameters apiParameters = description.getApiParameters();
        if (apiParameters != null) {
            props.putAll(apiParameters.toMap());
        }

        props.putAll(pluginParams.getOpts().toMap());
        return PluginResult.docElement("ApiParameters", props);
    }

    @Override
    public SearchText textForSearch() {
        ApiParameters apiParameters = member.getDescription().getApiParameters();
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
