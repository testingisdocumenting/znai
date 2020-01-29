package com.twosigma.znai.extensions.file;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
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
 *
 * sort of like a plugin trait. Maybe a good idea to formalize traits on plugins interface level.
 */
public class CodeReferencesTrait {
    private final ComponentsRegistry componentsRegistry;

    private final Path referencesFullPath;
    private final Path markupPath;
    private final String referencesPath;

    public CodeReferencesTrait(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;

        this.referencesPath = pluginParams.getOpts().get("referencesPath", null);
        this.referencesFullPath = referencesPath != null ?
                componentsRegistry.resourceResolver().fullPath(referencesPath):
                null;
    }

    public void updateProps(Map<String, Object> props) {
        if (!referencesProvided()) {
            return;
        }

        DocReferences references = buildReferences();
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
