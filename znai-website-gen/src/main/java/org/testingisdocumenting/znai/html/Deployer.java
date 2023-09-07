/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.html;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.website.WebResource;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public record Deployer(Path docRoot, Path deployRoot) {
    public Deployer(Path docRoot, Path deployRoot) {
        this.docRoot = docRoot.toAbsolutePath().normalize();
        this.deployRoot = deployRoot.toAbsolutePath().normalize();
    }

    public void deploy(String relativePath, byte[] content) {
        deploy(Paths.get(relativePath), content);
    }

    public void deploy(String relativePath, String content) {
        deploy(Paths.get(relativePath), content);
    }

    public void deploy(Path relativePath, String content) {
        deploy(relativePath.toString(), relativePath, content);
    }

    public void deploy(String originalPathForLogging, Path relativePath, String content) {
        deploy(originalPathForLogging, relativePath, content.getBytes(StandardCharsets.UTF_8));
    }

    public void deploy(Path srcPath) {
        Path deployPath = deployRoot.resolve(srcPath);
        printDeployMessage(srcPath, deployPath);

        if (!Files.exists(srcPath)) {
            throw new IllegalArgumentException("can't find file/dir: " + srcPath);
        }

        Path fullSrcPath = docRoot.resolve(srcPath);

        try {
            if (Files.isDirectory(fullSrcPath)) {
                FileUtils.copyDirectory(fullSrcPath.toFile(), deployPath.toFile());
            } else {
                FileUtils.copyFile(fullSrcPath.toFile(), deployPath.toFile());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deploy(WebResource webResource) {
        deploy(Paths.get(webResource.getPath()), webResource.getBinaryContent());
    }

    public void deploy(WebResource webResource, String content) {
        deploy(Paths.get(webResource.getPath()), content);
    }

    public void deploy(Path relativePath, byte[] content) {
        deploy(relativePath.toString(), relativePath, content);
    }

    public void deploy(String originalPathForLogging, Path relativePath, byte[] content) {
        final Path fullPath = deployRoot.resolve(relativePath);

        printDeployMessage(originalPathForLogging, fullPath);

        try {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printDeployMessage(Object from, Object to) {
        ConsoleOutputs.out("deploying ", Color.PURPLE, from, Color.BLACK, " to ", Color.PURPLE, to);
    }
}
