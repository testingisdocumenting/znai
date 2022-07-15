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

import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.python.PythonIncludeResultBuilder.*;
import static org.testingisdocumenting.znai.python.PythonIncludeResultBuilder.ArgsRenderOpt;
import static org.testingisdocumenting.znai.python.PythonIncludeResultBuilder.NameRenderOpt;

public class PythonClassIncludePlugin extends PythonIncludePluginBase {
    private PythonFileNameAndRelativeName fileAndRelativeEntryName;
    private PythonIncludeResultBuilder builder;
    private ParserHandler parserHandler;
    private PythonClass pythonClass;
    private List<PythonClass> pythonBaseClasses;
    private Path markupPath;
    private final Python python = Python.INSTANCE;

    @Override
    public String id() {
        return "python-class";
    }

    @Override
    public IncludePlugin create() {
        return new PythonClassIncludePlugin();
    }

    @Override
    protected String snippetIdToUse() {
        return pluginParams.getFreeParam();
    }

    @Override
    protected String fileNameToUse() {
        fileAndRelativeEntryName = PythonUtils.findFileNameAndRelativeNameByFullyQualifiedName(
                resourcesResolver,
                pluginParams.getFreeParam());

        return fileAndRelativeEntryName.getFileName();
    }

    @Override
    protected String defaultPackageName() {
        return fileAndRelativeEntryName.getPackageName();
    }

    @Override
    protected Stream<String> additionalAuxiliaryFileNames() {
        return pythonBaseClasses.stream().map(PythonClass::getFileName);
    }

    @Override
    public PythonIncludeResult process(PythonParsedFile parsed, ParserHandler parserHandler, Path markupPath) {
        this.parserHandler = parserHandler;
        this.markupPath = markupPath;

        pythonClass = parsed.findClassByName(fileAndRelativeEntryName.getRelativeName());
        pythonBaseClasses = collectAllBaseClasses(pythonClass);

        PythonParsedEntry classEntry = parsed.findRequiredEntryByTypeAndName("class", fileAndRelativeEntryName.getRelativeName());

        builder = new PythonIncludeResultBuilder(componentsRegistry,
                parserHandler,
                markupPath,
                pluginParams.getFreeParam());

        builder.addClassHeader();

        builder.addPyDocTextOnly(classEntry);
        builder.addBaseClassesLinks(pythonBaseClasses);

        addPropertiesSectionIfRequired();

        addProperties();
        pythonBaseClasses.forEach(this::addBaseProperties);

        addSignatures(pythonClass, false);
        pythonBaseClasses.forEach(c -> addSignatures(c, true));

        addDetails(pythonClass);

        return builder.build();
    }

    private List<PythonClass> collectAllBaseClasses(PythonClass pythonClass) {
        List<PythonClass> baseClasses = pythonClass.getBaseClasses().stream()
                .map(fullName -> python.parseClassOrGetCached(resourcesResolver, fullName))
                .collect(Collectors.toList());

        List<PythonClass> result = new ArrayList<>(baseClasses);
        baseClasses.forEach(pc -> result.addAll(collectAllBaseClasses(pc)));

        return result;
    }

    private void addSignatures(PythonClass pythonClass, boolean excludeInit) {
        builder.addSubSection(pythonBaseClasses.isEmpty() ? "Members" : pythonClass.getName() + " Members");
        addMembersSignature(pythonClass.getPackageName(), classMethods(pythonClass.getFunctions()));
        addMembersSignature(pythonClass.getPackageName(), staticMethods(pythonClass.getFunctions()));
        addMembersSignature(pythonClass.getPackageName(), regularMethods(pythonClass.getFunctions(), excludeInit));
    }

    private void addDetails(PythonClass pythonClass) {
        builder.addSubSection(pythonBaseClasses.isEmpty() ? "Details" : pythonClass.getName() + " Details");
        addMembersDetails(pythonClass.getPackageName(), classMethods(pythonClass.getFunctions()));
        addMembersDetails(pythonClass.getPackageName(), staticMethods(pythonClass.getFunctions()));
        addMembersDetails(pythonClass.getPackageName(), regularMethods(pythonClass.getFunctions(), false));
    }

    private void addPropertiesSectionIfRequired() {
        if (pythonClass.hasProperties() || pythonBaseClasses.stream().anyMatch(PythonClass::hasProperties)) {
            builder.addSubSection("Properties");
        }
    }

    private void addProperties() {
        addProperties(pythonClass, pythonClass.getBaseClasses().isEmpty() ? "" : pythonClass.getName());
    }

    private void addBaseProperties(PythonClass pythonClass) {
        addProperties(pythonClass, pythonClass.getName());
    }

    private void addProperties(PythonClass pythonClass, String title) {
        ApiParameters properties = pythonClass.createPropertiesAsApiParameters(componentsRegistry.docStructure(),
                componentsRegistry.markdownParser(),
                markupPath);

        if (!properties.isEmpty()) {
            Map<String, Object> props = properties.toMap();
            if (!title.isEmpty()) {
                props.put("title", title);
            }

            builder.addSearchText(properties.combinedTextForSearch());
            parserHandler.onCustomNode("ApiParameters", props);
        }
    }

    private Stream<PythonParsedEntry> classMethods(List<PythonParsedEntry> memberFunctions) {
        return memberFunctions.stream().filter(PythonParsedEntry::isClassMethod);
    }

    private Stream<PythonParsedEntry> staticMethods(List<PythonParsedEntry> memberFunctions) {
        return memberFunctions.stream().filter(PythonParsedEntry::isStatic);
    }

    private Stream<PythonParsedEntry> regularMethods(List<PythonParsedEntry> memberFunctions, boolean excludeInit) {
        return memberFunctions.stream().filter(e -> !e.isClassMethod() && !e.isStatic() && !e.isPrivate() &&
                !(excludeInit && e.getName().endsWith(".__init__")));
    }

    private void addMembersSignature(String packageName, Stream<PythonParsedEntry> entries) {
        entries.forEach(entry -> builder.addMethodSignature(packageName, entry,
                NameRenderOpt.SHORT_NAME, ArgsRenderOpt.REMOVE_SELF, MarginOpts.DEFAULT, true));
    }

    private void addMembersDetails(String packageName, Stream<PythonParsedEntry> entries) {
        entries.forEach(entry -> {
            builder.addEntryHeader(PythonUtils.entityNameFromQualifiedName(entry.getName()));
            String globalAnchorId = PythonUtils.globalAnchorId(packageName + "." + entry.getName());
            parserHandler.onGlobalAnchor(globalAnchorId);

            MarginOpts marginOpts = entry.getDocString().isEmpty() ? MarginOpts.DEFAULT: MarginOpts.EXTRA_BOTTOM_MARGIN;
            builder.addMethodSignature(packageName, entry, NameRenderOpt.FULL_NAME, ArgsRenderOpt.REMOVE_SELF, marginOpts, false);
            builder.addPyDocTextOnly(entry);
            builder.addPyDocParams(packageName, entry);
        });
    }
}
