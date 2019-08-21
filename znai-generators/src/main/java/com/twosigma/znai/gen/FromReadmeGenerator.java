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

package com.twosigma.znai.gen;

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
        gen.generate(cliConfig.getZnaiDest());
    }

    private FromReadmeGenerator(Path srcRoot, String sectionId) {
        this.srcRoot = srcRoot;
        this.sectionId = sectionId;
    }

    private void generate(Path znaiDest) throws IOException {
        Path znaiSectionPath = znaiDest.resolve(sectionId);
        readMeFiles = listReadMeFiles(srcRoot);
        copyMarkdowns(znaiSectionPath);

        String toc = generateToc();
        Files.write(znaiDest.resolve("toc"), toc.getBytes());

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
