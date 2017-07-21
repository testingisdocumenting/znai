package com.twosigma.documentation.html;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;

import org.apache.commons.io.FileUtils;

/**
 * @author mykola
 */
public class Deployer {
    private Path root;
    private Set<Path> deployed;

    public Deployer(Path root) {
        this.root = root.toAbsolutePath().normalize();
        deployed = new HashSet<>();
    }

    public void deploy(String relativePath, byte[] content) {
        deploy(Paths.get(relativePath), content);
    }

    public void deploy(String relativePath, String content) {
        deploy(Paths.get(relativePath), content);
    }

    public void deploy(Path relativePath, String content) {
        deploy(relativePath, content.getBytes(StandardCharsets.UTF_8));
    }

    public void deploy(Path srcPath) {
        printDeployMessage(srcPath, root);

        try {
            FileUtils.copyDirectory(srcPath.toFile(), root.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deploy(WebResource webResource) {
        deploy(Paths.get(webResource.getPath()), webResource.getContent());
    }

    public void deploy(WebResource webResource, String content) {
        deploy(Paths.get(webResource.getPath()), content);
    }

    public void deploy(Path relativePath, byte[] content) {
        final Path fullPath = root.resolve(relativePath);
        if (deployed.contains(fullPath))
            return;

        printDeployMessage(relativePath, fullPath);

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
