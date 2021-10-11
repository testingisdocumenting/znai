/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.cli.extension;

import java.nio.file.Path;

public class CliCommandConfig {
    private final String docId;
    private final Path sourceRoot;
    private final Path deployRoot;
    private final String actor;

    public CliCommandConfig(String docId, Path sourceRoot, Path deployRoot, String actor) {
        this.docId = docId;
        this.sourceRoot = sourceRoot;
        this.deployRoot = deployRoot;
        this.actor = actor;
    }

    public String getDocId() {
        return docId;
    }

    public boolean isDocIdSpecified() {
        return !getDocId().equals("no-id-specified");
    }

    public Path getSourceRoot() {
        return sourceRoot;
    }

    public Path getDeployRoot() {
        return deployRoot;
    }

    public String getActor() {
        return actor;
    }
    @Override
    public String toString() {
        return "CliCommandConfig{" +
                "docId='" + docId + '\'' +
                ", sourceRoot=" + sourceRoot +
                ", deployRoot=" + deployRoot +
                ", actor=" + actor +
                '}';
    }
}
