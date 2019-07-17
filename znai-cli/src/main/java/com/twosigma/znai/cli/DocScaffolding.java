package com.twosigma.znai.cli;

import com.twosigma.utils.FileUtils;
import com.twosigma.utils.ResourceUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocScaffolding {
    private final Path workingDir;
    private Map<String, List<String>> fileNameByDirName;

    public DocScaffolding(Path workingDir) {
        this.workingDir = workingDir;
        this.fileNameByDirName = new LinkedHashMap<>();
    }

    public void create() {
        createPages();
        createToc();
        createMeta();
        createIndex();
        createLookupPaths();
    }

    private void createLookupPaths() {
        createFileFromResource("lookup-paths");
    }

    private void createMeta() {
        createFileFromResource("meta.json");
    }

    private void createIndex() {
        createFileFromResource("index.md");
    }

    private void createPages() {
        createPage("chapter-one", "page-one");
        createPage("chapter-one", "page-two");
        createPage("chapter-two", "page-three");
        createPage("chapter-two", "page-four");
    }

    private void createToc() {
        String toc = fileNameByDirName.keySet().stream()
                .map(this::buildTocSection).collect(Collectors.joining("\n"));

        FileUtils.writeTextContent(workingDir.resolve("toc"), toc);
    }

    private void createFileFromResource(String fileName) {
        FileUtils.writeTextContent(workingDir.resolve(fileName), ResourceUtils.textContent(fileName));
    }

    private String buildTocSection(String dirName) {
        List<String> fileNames = fileNameByDirName.get(dirName);
        return dirName + "\n    " + fileNames.stream().collect(Collectors.joining("\n    "));
    }

    private void createPage(String dirName, String fileName) {
        registerPage(dirName, fileName);

        String withExtension = fileName + ".md";
        FileUtils.writeTextContent(workingDir.resolve(dirName).resolve(withExtension),
                ResourceUtils.textContent(withExtension));
    }

    private void registerPage(String dirName, String fileName) {
        List<String> fileNames = fileNameByDirName.computeIfAbsent(dirName, k -> new ArrayList<>());
        fileNames.add(fileName);
    }
}
