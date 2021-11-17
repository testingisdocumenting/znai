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
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DoxygenMemberIncludePlugin implements IncludePlugin {
    private List<DoxygenMember> membersList;
    private String fullName;
    private ComponentsRegistry componentsRegistry;
    private ParserHandler parserHandler;
    private Path markupPath;

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
        this.componentsRegistry = componentsRegistry;
        this.parserHandler = parserHandler;
        this.markupPath = markupPath;
        membersList = new ArrayList<>();
        Doxygen doxygen = Doxygen.INSTANCE;

        PluginParamsOpts paramsOpts = pluginParams.getOpts();
        fullName = pluginParams.getFreeParam();

        boolean includeAllMatches = paramsOpts.get("includeAllMatches", false);

        if (includeAllMatches) {
            membersList.addAll(doxygen.findAndParseAllMembers(componentsRegistry, fullName));
        } else {
            membersList.add(doxygen.getCachedOrFindAndParseMember(componentsRegistry,
                    fullName));
        }

        if (membersList.isEmpty()) {
            throw new RuntimeException("can't find member: " + fullName + ", available names:\n" +
                    doxygen.buildIndexOrGetCached(componentsRegistry).renderAvailableMemberNames());
        }

        if (paramsOpts.get("signatureOnly", false)) {
            return signatureOnly();
        }

        return fullDefinition();
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(fullName);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(Doxygen.INSTANCE.getIndexPath()));
    }

    private PluginResult signatureOnly() {
        membersList.forEach(this::memberAnchorAndSignature);
        return PluginResult.empty();
    }

    private PluginResult fullDefinition() {
        membersList.forEach(member -> {
            memberAnchorAndSignature(member);

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
        });

        return PluginResult.empty();
    }

    private void memberAnchorAndSignature(DoxygenMember member) {
        parserHandler.onGlobalAnchor(member.getId());
        parserHandler.onCustomNode("DoxygenMember", member.toMap());
    }
}
