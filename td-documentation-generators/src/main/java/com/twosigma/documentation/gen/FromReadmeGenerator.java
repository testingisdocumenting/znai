package com.twosigma.documentation.gen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class FromReadmeGenerator {
    private static List<Path> readMeFiles;
    private final Path srcRoot;
    private final String sectionId;

    public static void main(String[] args) throws IOException {
        CliConfig cliConfig = new CliConfig(args);

        FromReadmeGenerator gen = new FromReadmeGenerator(cliConfig.getReadmeRoot(), cliConfig.getSectionId());
        gen.generate(cliConfig.getMdocDest());
    }

    private FromReadmeGenerator(Path srcRoot, String sectionId) {
        this.srcRoot = srcRoot;
        this.sectionId = sectionId;
    }

    private void generate(Path mdocDest) throws IOException {
        Path mdocSectionPath = mdocDest.resolve(sectionId);
        readMeFiles = listReadMeFiles(srcRoot);
        copyMarkdowns(mdocSectionPath);

        String toc = generateToc();
        Files.write(mdocDest.resolve("toc"), toc.getBytes());

        System.out.println("generated toc:");
        System.out.println(toc);
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
