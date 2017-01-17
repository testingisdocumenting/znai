package com.twosigma.documentation.website.preview;

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
     * markdown files can refer other resources through include plugins,
     * if those resources are changed we need to refresh the md file that reference them
     */
    private Map<Path, Set<Path>> mdsByUsedFile;

    public PreviewFilesAssociationTracker() {
        mdsByUsedFile = new HashMap<>();
    }

    @Override
    public void onReset(final IncludeContext context) {
        mdsByUsedFile.remove(context.getCurrentFilePath());
    }

    @Override
    public void onInclude(final IncludePlugin plugin, final IncludeResult result) {
        final Path mdFile = result.getContext().getCurrentFilePath();
        result.getUsedFiles().forEach(uf -> addAssociation(mdFile, uf));
    }

    public Collection<Path> dependentMarkdowns(Path path) {
        final Set<Path> mds = mdsByUsedFile.get(path);
        if (mds == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableSet(mds);
    }

    private void addAssociation(Path mdFile, Path usedFile) {
        Set<Path> mds = mdsByUsedFile.get(usedFile);
        if (mds == null) {
            mds = new HashSet<>();
            mdsByUsedFile.put(usedFile, mds);
        }

        mds.add(mdFile);
    }
}
