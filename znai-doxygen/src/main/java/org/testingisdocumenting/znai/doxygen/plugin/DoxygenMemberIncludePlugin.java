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
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMember;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.Plugins;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

public class DoxygenMemberIncludePlugin implements IncludePlugin {
    private DoxygenMember member;

    @Override
    public String id() {
        return "doxygen-member";
    }

    @Override
    public IncludePlugin create() {
        return createMemberPlugin();
    }

    public static IncludePlugin createMemberPlugin() {
        return new DoxygenMemberIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        Doxygen doxygen = Doxygen.INSTANCE;

        PluginParamsOpts paramsOpts = pluginParams.getOpts();
        String fullName = pluginParams.getFreeParam();

        member = doxygen.getCachedOrFindAndParseMember(componentsRegistry,
                fullName);

        if (member == null) {
            throw new RuntimeException("can't find member: " + fullName + ", available names:\n" +
                    doxygen.buildIndexOrGetCached(componentsRegistry).renderAvailableMemberNames());
        }

        parserHandler.onGlobalAnchor(member.getId());

        if (paramsOpts.get("signatureOnly", false)) {
            return PluginResult.docElement("DoxygenMember", member.toMap());
        }

        parserHandler.onCustomNode("DoxygenMember", member.toMap());

        IncludePlugin docPlugin = DoxygenDocIncludePlugin.createDocPlugin();
        parserHandler.onIncludePlugin(docPlugin,
                docPlugin.process(componentsRegistry, parserHandler, markupPath,
                        new PluginParams(docPlugin.id(), fullName)));

        if (member.isFunction()) {
            IncludePlugin docParamsPlugin = DoxygenDocParamsIncludePlugin.createDocParamsPlugin();
            parserHandler.onIncludePlugin(docParamsPlugin,
                    docParamsPlugin.process(componentsRegistry, parserHandler, markupPath,
                            new PluginParams(docPlugin.id(), fullName, Collections.singletonMap("small", true))));
        }

        return PluginResult.docElements(Stream.empty());
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(member.getName());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(Doxygen.INSTANCE.getIndexPath()));
    }
}
