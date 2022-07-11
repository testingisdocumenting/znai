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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.python.pydoc.ParsedPythonDoc;
import org.testingisdocumenting.znai.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

class PythonIncludeResultBuilder {
    private static final HeadingProps entryNameHeadingProps = new HeadingProps(Collections.singletonMap("style", "api"));

    enum NameRenderOpt {
        FULL_NAME,
        SHORT_NAME
    }

    enum ArgsRenderOpt {
        REMOVE_SELF,
        KEEP_SELF
    }

    enum MarginOpts {
        EXTRA_BOTTOM_MARGIN,
        DEFAULT
    }

    private final List<String> searchText;
    private final ComponentsRegistry componentsRegistry;
    private final ParserHandler parserHandler;
    private final String qualifiedName;
    private final PythonUtils.FileNameAndRelativeName fileAndRelativeEntryName;

    public PythonIncludeResultBuilder(ComponentsRegistry componentsRegistry,
                                      ParserHandler parserHandler,
                                      String qualifiedName,
                                      PythonUtils.FileNameAndRelativeName fileAndRelativeEntryName) {
        this.componentsRegistry = componentsRegistry;
        this.parserHandler = parserHandler;
        this.qualifiedName = qualifiedName;
        this.fileAndRelativeEntryName = fileAndRelativeEntryName;
        searchText = new ArrayList<>();
    }

    public PythonIncludeResult build() {
        return new PythonIncludeResult(Collections.emptyList(), String.join(" ", searchText));
    }

    public void addClassHeader() {
        String globalAnchorId = PythonUtils.globalAnchorId(qualifiedName);
        parserHandler.onGlobalAnchor(globalAnchorId);
        parserHandler.onSectionStart(qualifiedName,
                new HeadingProps(CollectionUtils.createMap("badge", "class", "style", "api")));
    }

    public void addSubSection(String title) {
        parserHandler.onSubHeading(3, title, entryNameHeadingProps);
    }

    public void addEntryHeader(String name) {
        parserHandler.onSubHeading(4, name, entryNameHeadingProps);
    }

    public void addMethodSignature(PythonCodeEntry func,
                                   NameRenderOpt nameRenderOpt,
                                   ArgsRenderOpt argsRenderOpt,
                                   MarginOpts marginOpts,
                                   boolean attachUrl) {
        Map<String, Object> props = new LinkedHashMap<>(func.toMap(componentsRegistry.docStructure()));
        props.put("qualifiedName", fileAndRelativeEntryName.getPackageName() + "." + func.getName());

        if (nameRenderOpt == NameRenderOpt.SHORT_NAME) {
            props.put("hideNameQualifier", true);
        }

        if (argsRenderOpt == ArgsRenderOpt.REMOVE_SELF) {
            props.put("removeSelf", true);
        }

        if (marginOpts == MarginOpts.EXTRA_BOTTOM_MARGIN) {
            props.put("extraBottomMargin", true);
        }

        if (attachUrl) {
            Supplier<String> urlSupplier = () -> {
                Optional<String> globalAnchorUrl = componentsRegistry.docStructure().findGlobalAnchorUrl(
                        PythonUtils.globalAnchorId(fileAndRelativeEntryName.getPackageName() + "." + func.getName()));

                return globalAnchorUrl.orElse("");
            };
            props.put("url", urlSupplier);
        }

        parserHandler.onCustomNode("PythonMethod", props);
        searchText.add(fileAndRelativeEntryName.getRelativeName()); // TODO include parameter names into search
    }

    public void addPyDocTextOnly(Path markupPath, PythonCodeEntry func) {
        ParsedPythonDoc parsedPythonDoc = new ParsedPythonDoc(func.getDocString());

        componentsRegistry.markdownParser().parse(markupPath, parsedPythonDoc.getPyDocDescriptionOnly())
                .getDocElement().getContent().forEach(parserHandler::onDocElement);

        searchText.add(parsedPythonDoc.getPyDocDescriptionOnly());
    }

    public void addPyDocParams(Path markupPath, PythonCodeEntry func) {
        ApiParameters apiParameters = func.createParametersFromPyDoc(
                componentsRegistry.docStructure(),
                componentsRegistry.markdownParser(),
                markupPath,
                fileAndRelativeEntryName.getPackageName() + "_" + func.getName());

        if (apiParameters.isEmpty()) {
            return;
        }

        Map<String, Object> props = apiParameters.toMap();
        props.put("small", true);

        parserHandler.onCustomNode("ApiParameters", props);
        searchText.add(apiParameters.combinedTextForSearch());
    }
}
