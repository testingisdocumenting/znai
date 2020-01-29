package com.twosigma.znai.extensions.file;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.reference.DocReferences;
import com.twosigma.znai.reference.DocReferencesParser;

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
    private final String referencesPath;

    private final DocReferences references;

    public CodeReferencesTrait(ComponentsRegistry componentsRegistry, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;

        this.referencesPath = pluginParams.getOpts().get("referencesPath", null);
        this.referencesFullPath = referencesPath != null ?
                componentsRegistry.resourceResolver().fullPath(referencesPath):
                null;

        this.references = buildReferences();
    }

    public DocReferences getReferences() {
//        if (references == null) {
//            references = buildReferences();
//        }

        return references;
    }

    public void updateProps(Map<String, Object> props) {
        if (!referencesProvided()) {
            return;
        }

        props.put("references", getReferences().toMap());
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
