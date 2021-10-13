/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.cli;

import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.ResourceUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocScaffolding {
    private final Path workingDir;
    private final Map<String, List<String>> fileNameByDirName;

    public DocScaffolding(Path workingDir) {
        this.workingDir = workingDir;
        this.fileNameByDirName = new LinkedHashMap<>();
    }

    public void create() {
        createPages();
        createToc();
        createAuxiliaryContentFiles();
        createMeta();
        createIndex();
        createLookupPaths();
    }

    private void createLookupPaths() {
        createFileFromResource("lookup-paths");
    }

    private void createMeta() {
        createFileFromResource(DocMeta.META_FILE_NAME);
    }

    private void createIndex() {
        createFileFromResource("scaffold-index.md", "index.md");
    }

    private void createPages() {
        createPage("chapter-one", "getting-started");
        createPage("chapter-one", "page-two");
        createPage("chapter-two", "page-three");
        createPage("chapter-two", "page-four");
        createFooter();
    }

    private void createToc() {
        String toc = fileNameByDirName.keySet().stream()
                .map(this::buildTocSection).collect(Collectors.joining("\n"));

        FileUtils.writeTextContent(workingDir.resolve("toc"), toc);
    }

    private void createAuxiliaryContentFiles() {
        createAuxiliaryContentFile("file-name.js");
    }

    private void createFileFromResource(String resourceName) {
        createFileFromResource(resourceName, resourceName);
    }

    private void createFileFromResource(String resourceName, String destName) {
        FileUtils.writeTextContent(workingDir.resolve(destName), ResourceUtils.textContent(resourceName));
    }

    private String buildTocSection(String dirName) {
        List<String> fileNames = fileNameByDirName.get(dirName);
        return dirName + "\n    " + String.join("\n    ", fileNames);
    }

    private void createPage(String dirName, String fileName) {
        registerPage(dirName, fileName);

        String withExtension = fileName + ".md";
        FileUtils.writeTextContent(workingDir.resolve(dirName).resolve(withExtension),
                ResourceUtils.textContent(withExtension));
    }

    private void createFooter() {
        String footerName = "footer.md";
        FileUtils.writeTextContent(workingDir.resolve(footerName),
                ResourceUtils.textContent(footerName));
    }

    private void registerPage(String dirName, String fileName) {
        List<String> fileNames = fileNameByDirName.computeIfAbsent(dirName, k -> new ArrayList<>());
        fileNames.add(fileName);
    }

    private void createAuxiliaryContentFile(String resourceFilePath) {
        FileUtils.writeTextContent(workingDir.resolve(resourceFilePath), ResourceUtils.textContent(resourceFilePath));
    }
}
