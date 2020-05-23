/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.enterprise.monitoring;

import org.apache.commons.codec.digest.DigestUtils;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.fs.FsUtils;
import org.testingisdocumenting.znai.server.ServerLifecycleListener;
import org.testingisdocumenting.znai.server.ZnaiServerConfig;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.documentationStorage;
import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.enterpriseConfig;

public class BuildOutputMonitor implements ServerLifecycleListener {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final FilesFinder filesFinder;
    private final MonitorConfig monitorConfig;

    private final Map<Path, String> checkSumByPath;

    public BuildOutputMonitor() {
        this(parseMonitoringConfig());
    }

    public BuildOutputMonitor(MonitorConfig monitorConfig) {
        this.filesFinder = new FilesFinder(monitorConfig.getBuildRootsAndPatterns());
        this.monitorConfig = monitorConfig;
        this.checkSumByPath = new HashMap<>();
    }

    @Override
    public void beforeStart(ZnaiServerConfig config) {
        start();
    }

    public void start() {
        executorService.scheduleAtFixedRate(this::scan, 0, monitorConfig.getIntervalMillis(), TimeUnit.MILLISECONDS);
    }

    private void scan() {
        List<Path> found = filesFinder.find();
        found.stream()
                .filter(this::isChecksumDifferent)
                .forEach(this::process);
    }

    private void process(Path zip) {
        try {
            unzipAndStore(zip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unzipAndStore(Path zip) {
        Path tempDir = FsUtils.createTempDir("znai-unzipped-doc");

        FsUtils.unzip(zip, tempDir);

        Path docsDir = listFiles(tempDir)
                .filter(Files::isDirectory)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no directory found inside zip"));

        String docId = docsDir.getFileName().toString();

        ConsoleOutputs.out("detected ", Color.WHITE, docId, Color.BLUE, " at ", Color.PURPLE, zip);
        documentationStorage().store(docId, "", docsDir);

        FsUtils.deleteDirectory(tempDir);
    }

    private boolean isChecksumDifferent(Path path) {
        String previousCheckSum = checkSumByPath.get(path);
        String newCheckSum = checkSum(path);

        checkSumByPath.put(path, newCheckSum);

        if (previousCheckSum == null) {
            return true;
        }

        return !previousCheckSum.equals(newCheckSum);
    }

    private String checkSum(Path path) {
        try {
            return DigestUtils.md5Hex(Files.newInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Path> listFiles(Path dir) {
        try {
            return Files.list(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static MonitorConfig parseMonitoringConfig() {
        Path fsMonitorConfigPath = enterpriseConfig().getFsMonitorConfigPath();
        if (!Files.exists(fsMonitorConfigPath)) {
            return new MonitorConfig(Collections.emptyMap());
        }

        Map<String, ?> config = JsonUtils.deserializeAsMap(FileUtils.fileTextContent(
                fsMonitorConfigPath));

        return new MonitorConfig((Map<String, Object>) config);
    }
}
