package com.twosigma.documentation.core;

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

    /**
     * file that is required at documentation hosting time and thus it will be copied to the deployment
     * @param path file path
     * @return auxiliary file instance
     */
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

    @Override
    public String toString() {
        return "AuxiliaryFile{" +
                "path=" + path +
                ", requiresDeployment=" + requiresDeployment +
                '}';
    }
}
