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

package org.testingisdocumenting.znai.structure;

import org.testingisdocumenting.znai.utils.FilePathUtils;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.nio.file.Path;

public class DocUrl {
    private static final String LINK_TO_SECTION_INSTRUCTION = """
            To refer to a section of a document page use either
              dir-name/file-name-without-extension#page-section-id or
              ../dir-name/file-name.md#page-section-id or
              ./file-name-within-chapter.md#page-section-id
              (#page-section-id is optional)
            dir-name is not an arbitrary directory name or structure, but the TOC directory associated with a chapter
            Use #page-section-id to refer to the current page section.
            Use /#section-id to refer the root page of a documentation.
            """;

    private String dirName = "";
    private String fileNameWithoutExtension = "";
    private String anchorId = "";

    private String url;

    private boolean isExternalUrl;
    private boolean isAnchorOnly;
    private boolean isIndexUrl;

    public static DocUrl indexUrl() {
        return new DocUrl(true);
    }

    private DocUrl(boolean isIndexUrl) {
        this.isIndexUrl = isIndexUrl;
    }

    public DocUrl(String dirName, String fileNameWithoutExtension, String anchorId) {
        this.dirName = dirName;
        this.fileNameWithoutExtension = fileNameWithoutExtension;
        this.anchorId = anchorId;
    }

    public DocUrl(Path markupPath, String url) {
        this.url = url;

        boolean handled = handleExternal() ||
                handleIndex() ||
                handleAnchorOnly() ||
                handleLocal(markupPath);

        if (!handled) {
            throw new IllegalStateException("couldn't parse url: " + url);
        }
    }

    private boolean handleExternal() {
        return isExternalUrl = UrlUtils.isExternal(url);
    }

    private boolean handleIndex() {
        if (url.startsWith("/#")) {
            isIndexUrl = true;
            anchorId = url.substring(2);

            return true;
        }

        return isIndexUrl = url.equals("/");
    }

    private boolean handleAnchorOnly() {
        isAnchorOnly = url.startsWith("#");
        if (isAnchorOnly) {
            dirName = "";
            fileNameWithoutExtension = "";
            anchorId = url.substring(1);
        }

        return isAnchorOnly;
    }

    private boolean handleLocal(Path markupPath) {
        String[] parts = url.split("/");
        if (parts.length == 1) {
            return handleNoDirSpecified(markupPath, parts[0]);
        }

        if (parts.length != 2 && parts.length != 3) {
            throw new IllegalArgumentException("Unexpected url pattern: <" + url + "> " + LINK_TO_SECTION_INSTRUCTION);
        }

        if (parts.length == 3 && !parts[0].equals("..")) {
            throw new IllegalArgumentException("Unexpected url pattern: <" + url + "> " + LINK_TO_SECTION_INSTRUCTION);
        }

        int dirIdx = parts.length == 3 ? 1 : 0;
        int nameIdx = parts.length == 3 ? 2 : 1;

        dirName = replaceDirNameIfRequired(markupPath, parts[dirIdx]);

        int idxOfAnchorSep = parts[nameIdx].indexOf('#');

        fileNameWithoutExtension = FilePathUtils.fileNameWithoutExtension(idxOfAnchorSep == -1 ? parts[nameIdx] : parts[nameIdx].substring(0, idxOfAnchorSep));
        anchorId = idxOfAnchorSep == -1 ? "" : parts[nameIdx].substring(idxOfAnchorSep + 1);

        return true;
    }

    private boolean handleNoDirSpecified(Path markupPath, String part) {
        int idxOfAnchorSep = part.indexOf('#');
        dirName = replaceDirNameIfRequired(markupPath, "");
        fileNameWithoutExtension = FilePathUtils.fileNameWithoutExtension(idxOfAnchorSep == -1 ? part : part.substring(0, idxOfAnchorSep));
        anchorId = idxOfAnchorSep == -1 ? "" : part.substring(idxOfAnchorSep + 1);

        return true;
    }

    private static String replaceDirNameIfRequired(Path markupPath, String currentDirName) {
        if (currentDirName.equals(".") || currentDirName.isEmpty()) {
            Path parentPath = markupPath.getParent();
            return parentPath != null ? parentPath.getFileName().toString() : "";
        } else {
            return currentDirName;
        }
    }

    public boolean isIndexUrl() {
        return isIndexUrl;
    }

    public boolean isExternalUrl() {
        return isExternalUrl;
    }

    public boolean isAnchorOnly() {
        return isAnchorOnly;
    }

    public String getDirName() {
        return dirName;
    }

    public String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public String getAnchorIdWithHash() {
        return anchorId.isEmpty() ?  "" : "#" + anchorId;
    }

    public String getUrl() {
        return url;
    }
}
