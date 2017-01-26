package com.twosigma.documentation.server.preview;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludeResult;
import com.twosigma.documentation.extensions.PluginsListener;

/**
 * @author mykola
 */
public class PreviewFilesAssociationTracker implements PluginsListener {
    /**
     * markup files can refer other resources through include plugins,
     * if those resources are changed we need to refresh the md file that reference them
     */
    private Map<Path, Set<Path>> markupsByUsedFile;

    public PreviewFilesAssociationTracker() {
        markupsByUsedFile = new HashMap<>();
    }

    @Override
    public void onReset(final IncludeContext context) {
        markupsByUsedFile.remove(context.getCurrentFilePath());
    }

    @Override
    public void onInclude(final IncludePlugin plugin, final IncludeResult result) {
        final Path markupFile = result.getContext().getCurrentFilePath();
        result.getUsedFiles().forEach(uf -> addAssociation(markupFile, uf));
    }

    public Collection<Path> dependentMarkups(Path path) {
        final Set<Path> markups = markupsByUsedFile.get(path);
        if (markups == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableSet(markups);
    }

    private void addAssociation(Path markupFile, Path usedFile) {
        Set<Path> markups = markupsByUsedFile.get(usedFile);
        if (markups == null) {
            markups = new HashSet<>();
            markupsByUsedFile.put(usedFile, markups);
        }

        markups.add(markupFile);
    }
}
