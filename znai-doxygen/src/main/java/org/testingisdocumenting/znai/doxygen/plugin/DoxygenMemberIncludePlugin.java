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

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.doxygen.Doxygen;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenDescription;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMember;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMembersList;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class DoxygenMemberIncludePlugin implements IncludePlugin {
    private Boolean disableAnchor;
    private DoxygenMembersList membersList;
    private ComponentsRegistry componentsRegistry;
    private ParserHandler parserHandler;
    private Path markupPath;
    private String fullName;
    private PluginParamsOpts paramsOpts;

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

        paramsOpts = pluginParams.getOpts();
        fullName = pluginParams.getFreeParam();
        membersList = DoxygenMemberListExtractor.extract(Doxygen.INSTANCE, componentsRegistry,
                paramsOpts, true, fullName);

        if (membersList.isEmpty()) {
            DoxygenMemberListExtractor.throwIfMembersListIsEmpty(Doxygen.INSTANCE, componentsRegistry, fullName);
        }
        disableAnchor = paramsOpts.get("disableAnchor", false);

        if (paramsOpts.get("signatureOnly", false)) {
            return signatureOnly();
        }

        return fullDefinition();
    }

    private PluginResult signatureOnly() {
        membersList.forEach(this::memberAnchorAndSignature);
        return PluginResult.empty();
    }

    private PluginResult fullDefinition() {
        membersList.forEach(this::fullDefinition);
        return PluginResult.empty();
    }

    private void fullDefinition(DoxygenMember member) {
        memberAnchorAndSignature(member);

        DoxygenDescription description = member.getDescription();
        boolean hasParametersDesc = description.getFull() != null &&
                description.getFull().getApiParameters() != null;
        boolean hasTemplateParametersDesc = description.getFull() != null &&
                description.getFull().getApiTemplateParameters() != null;

        IncludePlugin docPlugin = DoxygenDocIncludePlugin.createDocPlugin();
        parserHandler.onIncludePlugin(docPlugin,
                docPlugin.process(componentsRegistry, parserHandler, markupPath,
                        componentsRegistry.pluginParamsFactory().create(docPlugin.id(), fullName, paramsOpts.toMap())));

        if (hasParametersDesc) {
            parameters("", hasTemplateParametersDesc ? "parameters" : "");
        }

        if (hasTemplateParametersDesc) {
            parameters("template", hasParametersDesc ? "template parameters" : "");
        }
    }

    private void parameters(String type, String title) {
        IncludePlugin docParamsPlugin = DoxygenDocParamsIncludePlugin.createDocParamsPlugin();
        Map<String, Object> paramsOpts = this.paramsOpts.toMap();
        paramsOpts.put("small", true);
        paramsOpts.put("type", type);

        if (!title.isEmpty()) {
            paramsOpts.put("title", title);
        }

        parserHandler.onIncludePlugin(docParamsPlugin,
                docParamsPlugin.process(componentsRegistry, parserHandler, markupPath,
                        componentsRegistry.pluginParamsFactory().create(docParamsPlugin.id(), fullName, paramsOpts)));
    }

    private void memberAnchorAndSignature(DoxygenMember member) {
        if (!disableAnchor) {
            parserHandler.onGlobalAnchor(member.getId());
        }

        parserHandler.onCustomNode("DoxygenMember", member.toMap());
    }

    @Override
    public List<SearchText> textForSearch() {
        if (membersList == null || membersList.isEmpty()) {
            return List.of();
        }

        return List.of(SearchScore.HIGH.text(membersList.first().getDescription().textForSearch()));
    }
}
