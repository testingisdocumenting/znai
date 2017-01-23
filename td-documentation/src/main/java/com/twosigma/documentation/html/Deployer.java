package com.twosigma.documentation.html;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

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
        deploy(Paths.get(relativePath), content.getBytes());
    }

    public void deploy(Path relativePath, String content) {
        deploy(relativePath, content.getBytes());
    }

    public void deploy(Path srcPath) {
        System.out.println("deploying " + srcPath + " to " + root);

        try {
            FileUtils.copyDirectory(srcPath.toFile(), root.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deploy(WebResource webResource) {
        deploy(Paths.get(webResource.getPath()), webResource.getContent());
    }

    public void deploy(Path relativePath, byte[] content) {
        final Path fullPath = root.resolve(relativePath);
        if (deployed.contains(fullPath))
            return;

        System.out.println("deploying " + relativePath + " to " + fullPath);

        try {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
