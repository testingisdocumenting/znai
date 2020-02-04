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

package com.twosigma.znai.structure;

import java.nio.file.Path;

public interface DocStructure {
    void validateUrl(Path path, String additionalClue, DocUrl docUrl);
    String createUrl(Path path, DocUrl docUrl);
    String fullUrl(String relativeUrl);

    void registerGlobalAnchor(Path sourcePath, String anchorId);
    void registerLocalAnchor(Path path, String anchorId);
    String globalAnchorUrl(Path clientPath, String anchorId);

    TableOfContents tableOfContents();
}
