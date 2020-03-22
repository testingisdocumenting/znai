/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.core;

import java.nio.file.Path;
import java.util.Objects;

public class AuxiliaryFile {
    private final Path path;
    private Path deployRelativePath;
    private boolean requiresDeployment;

    public static AuxiliaryFile builtTime(Path path) {
        return new AuxiliaryFile(path, null,false);
    }

    /**
     * file that is required at documentation hosting time and thus it will be copied to the deployment
     * @param path file path
     * @param deployRelativePath relative path to use for deployment
     * @return auxiliary file instance
     */
    public static AuxiliaryFile runTime(Path path, Path deployRelativePath) {
        return new AuxiliaryFile(path, deployRelativePath,true);
    }

    private AuxiliaryFile(Path path, Path deployRelativePath, boolean requiresDeployment) {
        this.path = path;
        this.deployRelativePath = deployRelativePath;
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
        return requiresDeployment == that.requiresDeployment &&
                Objects.equals(path, that.path) &&
                Objects.equals(deployRelativePath, that.deployRelativePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, deployRelativePath, requiresDeployment);
    }

    public Path getPath() {
        return path;
    }

    public boolean isDeploymentRequired() {
        return requiresDeployment;
    }

    public Path getDeployRelativePath() {
        return deployRelativePath;
    }

    @Override
    public String toString() {
        return "AuxiliaryFile{" +
                "path=" + path +
                ", deployRelativePath='" + deployRelativePath + '\'' +
                ", requiresDeployment=" + requiresDeployment +
                '}';
    }
}
