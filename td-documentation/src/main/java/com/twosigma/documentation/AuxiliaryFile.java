package com.twosigma.documentation;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class AuxiliaryFile {
    private Path path;
    private boolean requiresDeployment;

    public static AuxiliaryFile builtTime(Path path) {
        return new AuxiliaryFile(path, false);
    }

    public static AuxiliaryFile runTime(Path path) {
        return new AuxiliaryFile(path, true);
    }

    private AuxiliaryFile(Path path, boolean requiresDeployment) {
        this.path = path;
        this.requiresDeployment = requiresDeployment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuxiliaryFile that = (AuxiliaryFile) o;

        if (requiresDeployment != that.requiresDeployment) {
            return false;
        }

        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + (requiresDeployment ? 1 : 0);

        return result;
    }

    public Path getPath() {
        return path;
    }

    public boolean isRequiresDeployment() {
        return requiresDeployment;
    }
}
