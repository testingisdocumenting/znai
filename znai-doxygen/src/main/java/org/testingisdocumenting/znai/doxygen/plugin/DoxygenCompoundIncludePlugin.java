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
import java.util.Map;
import java.util.stream.Stream;

public class DoxygenCompoundIncludePlugin implements IncludePlugin {
    private DoxygenCompound compound;

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

        HeadingProps headingProps = new HeadingProps(Collections.singletonMap("style", "api"));

        parserHandler.onSubHeading(2, "functions", headingProps);
        compound.membersStream().forEach(member -> {
            Map<String, Object> memberProps = member.toMap();
            memberProps.put("compoundName", ""); // to remove compound name from rendering list of members
            memberProps.put("refId", member.getId()); // to make clickable, as in TOC for members

            parserHandler.onCustomNode("DoxygenMember", memberProps);
        });

        parserHandler.onSubHeading(2, "definitions", headingProps);
        compound.membersStream().forEach(member -> {
            parserHandler.onSubHeading(3, member.getName(), headingProps);
            parserHandler.onGlobalAnchor(member.getId());
            parserHandler.onCustomNode("DoxygenMember", member.toMap());
        });

        return PluginResult.docElements(Stream.empty());
    }

    @Override
    public SearchText textForSearch() {
        // TODO include all texts from all members, etc
        return SearchScore.HIGH.text(compound.getName());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(Doxygen.INSTANCE.getIndexPath()));
    }
}
