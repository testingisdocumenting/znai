package org.testingisdocumenting.znai.diagrams.mermaid;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.Plugin;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

abstract class MermaidPluginBase implements Plugin {
    protected Map<AuxiliaryFile, Boolean> additionalAuxiliaryFiles = new HashMap<>();

    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return additionalAuxiliaryFiles.entrySet().stream().filter(e -> e.getValue() == Boolean.FALSE).map(Map.Entry::getKey);
    }

    protected void processIconPacks(ComponentsRegistry componentsRegistry, Map<String, Object> props) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        DocStructure docStructure = componentsRegistry.docStructure();
        if (!props.containsKey("iconpacks")) {
            return;
        }
        if ((props.get("iconpacks") instanceof List<?>)) {
            List<?> iconPacks = (List<?>) props.get("iconpacks");
            iconPacks.forEach(iconPack -> {
                        tweakIconpackUrl(resourcesResolver, docStructure, iconPack);
                    }
            );
        }
    }

    private void tweakIconpackUrl(ResourcesResolver resourcesResolver, DocStructure docStructure, Object iconPack) {
        if (!(iconPack instanceof Map<?, ?>)) {
            return;
        }
        @SuppressWarnings("unchecked") Map<String, String> iconPackMap = (Map<String, String>) iconPack;
        if (iconPackMap.containsKey("url")) {
            String url = iconPackMap.get("url");
            if (!UrlUtils.isExternal(url)) {
                AuxiliaryFile auxiliaryFile = resourcesResolver.runtimeAuxiliaryFile(url);
                additionalAuxiliaryFiles.put(auxiliaryFile, false);
                url = docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString());
            }
            iconPackMap.put("url", url);
        }
    }
}
