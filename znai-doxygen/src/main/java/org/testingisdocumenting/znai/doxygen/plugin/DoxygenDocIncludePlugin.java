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
import org.testingisdocumenting.znai.doxygen.parser.DoxygenMembersList;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.stream.Stream;

public class DoxygenDocIncludePlugin implements IncludePlugin {
    private DoxygenMembersList membersList;
    private DoxygenCompound compound;

    @Override
    public String id() {
        return "doxygen-doc";
    }

    @Override
    public IncludePlugin create() {
        return createDocPlugin();
    }

    public static IncludePlugin createDocPlugin() {
        return new DoxygenDocIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        Doxygen doxygen = Doxygen.INSTANCE;
        membersList = new DoxygenMembersList();

        String fullName = pluginParams.getFreeParam();
        compound = doxygen.getCachedOrFindAndParseCompound(componentsRegistry,
                fullName);

        if (compound == null) {
            membersList = DoxygenMemberListExtractor.extract(doxygen, componentsRegistry,
                    pluginParams.getOpts(), false, pluginParams.getFreeParam());
        }

        if (compound == null && membersList.isEmpty()) {
            throw new RuntimeException("can't find entry: " + fullName + ", available names:\n" +
                    doxygen.buildIndexOrGetCached(componentsRegistry).renderAvailableNames());
        }

        if (compound != null) {
            return PluginResult.docElements(compound.getDescription().docElementStream());
        }

        return PluginResult.docElements(membersList.first().getDescription().docElementStream());
    }

    @Override
    public SearchText textForSearch() {
        return membersList.isEmpty() ?
                SearchScore.HIGH.text(compound.getDescription().textForSearch()):
                SearchScore.HIGH.text(membersList.first().getDescription().textForSearch());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(Doxygen.INSTANCE.getIndexPath()));
    }
}
