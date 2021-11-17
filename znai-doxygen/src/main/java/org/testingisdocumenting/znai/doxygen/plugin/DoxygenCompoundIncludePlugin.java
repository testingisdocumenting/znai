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
import org.testingisdocumenting.znai.doxygen.parser.DoxygenCompound;
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMember;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoxygenCompoundIncludePlugin implements IncludePlugin {
    private static final HeadingProps headingProps = new HeadingProps(Collections.singletonMap("style", "api"));
    private DoxygenCompound compound;
    private ComponentsRegistry componentsRegistry;
    private ParserHandler parserHandler;
    private Path markupPath;

    @Override
    public String id() {
        return "doxygen-compound";
    }

    @Override
    public IncludePlugin create() {
        return new DoxygenCompoundIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;
        this.parserHandler = parserHandler;
        this.markupPath = markupPath;
        Doxygen doxygen = Doxygen.INSTANCE;

        String fullName = pluginParams.getFreeParam();

        compound = doxygen.getCachedOrFindAndParseCompound(componentsRegistry, fullName);

        if (compound == null) {
            throw new RuntimeException("can't find compound: " + fullName + ", available names:\n" +
                    doxygen.buildIndexOrGetCached(componentsRegistry).renderAvailableCompoundNames());
        }

        parserHandler.onGlobalAnchor(compound.getId());
        parserHandler.onSectionStart(compound.getName(),
                new HeadingProps(CollectionUtils.createMap("badge", compound.getKind(), "style", "api")));

        insertDocs(fullName);

        declBlock("Public Functions", compound.publicNonStaticFunctionsStream());
        declBlock("Static Public Functions", compound.publicStaticFunctionsStream());
        declBlock("Public Attributes", compound.publicNonStaticAttributesStream());
        declBlock("Static Public Attributes", compound.publicStaticAttributesStream());
        declBlock("Protected Functions", compound.protectedFunctionsStream());

        parserHandler.onSubHeading(3, "Definitions", headingProps);
        compound.publicNonStaticFunctionsStream().forEach(this::createMemberDef);
        compound.publicStaticFunctionsStream().forEach(this::createMemberDef);
        compound.publicNonStaticAttributesStream().forEach(this::createMemberDef);
        compound.publicStaticAttributesStream().forEach(this::createMemberDef);
        compound.protectedFunctionsStream().forEach(this::createMemberDef);

        return PluginResult.docElements(Stream.empty());
    }

    private void declBlock(String name, Stream<DoxygenMember> memberStream) {
        List<DoxygenMember> members = memberStream.collect(Collectors.toList());
        if (members.isEmpty()) {
            return;
        }

        parserHandler.onSubHeading(3, name, headingProps);
        members.forEach(this::createMemberDecl);
    }

    private void insertDocs(String fullName) {
        IncludePlugin includePlugin = DoxygenDocIncludePlugin.createDocPlugin();
        PluginResult pluginResult = includePlugin.process(componentsRegistry, parserHandler, markupPath,
                new PluginParams(includePlugin.id(), fullName));
        parserHandler.onIncludePlugin(includePlugin, pluginResult);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(compound.getName());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(Doxygen.INSTANCE.getIndexPath()));
    }

    public void createMemberDecl(DoxygenMember member) {
        Map<String, Object> memberProps = member.toMap();
        memberProps.put("compoundName", ""); // to remove compound name from rendering list of members
        memberProps.put("refId", member.getId()); // to make clickable, as in TOC for members

        parserHandler.onCustomNode("DoxygenMember", memberProps);
    }

    public void createMemberDef(DoxygenMember member) {
        parserHandler.onSubHeading(4, member.getName(), headingProps);

        IncludePlugin includePlugin = DoxygenMemberIncludePlugin.createMemberPlugin();
        PluginResult pluginResult = includePlugin.process(componentsRegistry, parserHandler, markupPath,
                new PluginParams(includePlugin.id(), member.getFullName()));
        parserHandler.onIncludePlugin(includePlugin, pluginResult);
    }
}
