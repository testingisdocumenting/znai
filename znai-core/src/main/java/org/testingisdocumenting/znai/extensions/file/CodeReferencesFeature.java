package com.twosigma.znai.extensions.file;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.features.PluginFeature;
import com.twosigma.znai.reference.DocReferences;
import com.twosigma.znai.reference.DocReferencesParser;
import com.twosigma.znai.structure.DocStructure;
import com.twosigma.znai.structure.DocUrl;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

/**
 * common code for the include-file plugin, include-java plugin, etc
 * to handle parsing of code references, converting them to props, and handling auxiliary files.
 */
public class CodeReferencesFeature implements PluginFeature {
    private final ComponentsRegistry componentsRegistry;

    private final Path referencesFullPath;
    private final String referencesPath;

    private final Path markupPath;
    private final DocReferences references;

    public CodeReferencesFeature(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;

        this.referencesPath = pluginParams.getOpts().get("referencesPath", null);
        this.referencesFullPath = referencesPath != null ?
                componentsRegistry.resourceResolver().fullPath(referencesPath):
                null;

        this.references = buildReferences();
    }

    public DocReferences getReferences() {
        return references;
    }

    public void updateProps(Map<String, Object> props) {
        if (!referencesProvided()) {
            return;
        }

        validateLinks(references);
        props.put("references", references.toMap());
    }

    private void validateLinks(DocReferences references) {
        DocStructure docStructure = componentsRegistry.docStructure();

        references.pageUrlsStream().forEach(pageUrl ->
                docStructure.validateUrl(markupPath,
                        "reference file name: " + referencesFullPath.getFileName().toString(),
                        new DocUrl(pageUrl))
        );
    }

    private DocReferences buildReferences() {
        if (referencesPath == null) {
            return DocReferences.EMPTY;
        }

        return DocReferencesParser.parse(
                componentsRegistry.resourceResolver().textContent(referencesPath));
    }

    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return referencesProvided() ?
                Stream.of(AuxiliaryFile.builtTime(referencesFullPath)) : Stream.empty();
    }

    private boolean referencesProvided() {
        return referencesFullPath != null;
    }
}
