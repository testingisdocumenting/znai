package com.twosigma.documentation.gen;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class GenFromReadme {
    private static List<Path> readMeFiles;
    private final Path srcRoot;
    private final String sectionId;
    private Path mdoc;

    public static void main(String[] args) throws IOException {
        Path root = Paths.get("/Users/mykola/work/testing-documenting/readme");

        GenFromReadme gen = new GenFromReadme(root, "data-refernce");
        gen.generate();
    }

    public GenFromReadme(Path srcRoot, String sectionId) {
        this.srcRoot = srcRoot;
        this.sectionId = sectionId;
    }

    public void generate() throws IOException {
        mdoc = srcRoot.getParent().resolve("mdoc-readme-test");
        Path mdocSectionPath = mdoc.resolve(sectionId);
        readMeFiles = listReadMeFiles(srcRoot);
        copyMarkdowns(mdocSectionPath);

        String toc = generateToc();
        System.out.println(toc);
        Files.write(mdoc.resolve("toc"), toc.getBytes());
    }

    private void copyMarkdowns(Path dest) throws IOException {
        readMeFiles.forEach(readMe -> {
            Path destFile = dest.resolve(readMe.getParent().getFileName() + ".md");
            try {
                Files.createDirectories(destFile.getParent());
                Files.copy(readMe, destFile, REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String generateToc() {
        return sectionId + "\n" +
                readMeFiles.stream().map(p -> "    " + p.getParent().getFileName().toString()).
                        collect(joining("\n"));
    }

    private List<Path> listReadMeFiles(Path srcRoot) throws IOException {
        return Files.list(srcRoot)
                    .filter(Files::isDirectory)
                    .map(p -> p.resolve("README.md"))
                    .filter(Files::exists)
                    .collect(toList());
    }
}
