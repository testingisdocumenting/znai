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

public class DocUrl {
    private static final String LINK_TO_SECTION_INSTRUCTION = """
            To refer to a section of a document page use either
              dir-name/file-name-without-extension#page-section-id or
              (#page-section-id is optional)
            dir-name is not an arbitrary directory name or structure, but the TOC directory associated with a chapter
            Use #page-section-id to refer to the current page section.
            Use /#section-id to refer the root page of a documentation.
            
            Alternatively you can use a relative path to another markdown file.
            Path can be relative to the root of the documentation or to the current file.
            """;

    private String dirName = "";
    private String fileNameWithoutExtension = "";
    private String anchorId = "";
    private String tocItemFilePath = "";

    private String url;

    private boolean isExternalUrl;
    private boolean isAnchorOnly;
    private boolean isIndexUrl;

    public DocUrl(String dirName, String fileNameWithoutExtension, String anchorId) {
        this.dirName = dirName;
        this.fileNameWithoutExtension = fileNameWithoutExtension;
        this.anchorId = anchorId;
    }

    public DocUrl(String url) {
        this.url = url;

        boolean handled = handleExternal() ||
                handleBasedOnFilePath(url) ||
                handleLocalFile(url) ||
                handleIndex() ||
                handleAnchorOnly() ||
                handleLocal();

        if (!handled) {
            throw new IllegalStateException("couldn't parse url: " + url);
        }
    }

    private boolean handleExternal() {
        return isExternalUrl = UrlUtils.isExternal(url);
    }

    private boolean handleLocalFile(String url) {
        return !FilePathUtils.fileExtension(url).isEmpty();
    }

    private boolean handleBasedOnFilePath(String url) {
        String withoutAnchor = UrlUtils.removeAnchor(url);
        String extension = FilePathUtils.fileExtension(withoutAnchor);

        if (extension.startsWith("md")) {
            tocItemFilePath = withoutAnchor;
            anchorId = UrlUtils.extractAnchor(url);
            return true;
        }

        return false;
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

    private boolean handleLocal() {
        String[] parts = url.split("/");
        if (parts.length != 2 && parts.length != 3) {
            throw new IllegalArgumentException("Unexpected url pattern: <" + url + "> " + LINK_TO_SECTION_INSTRUCTION);
        }

        if (parts.length == 3 && !parts[0].equals("..")) {
            throw new IllegalArgumentException("Unexpected url pattern: <" + url + "> " + LINK_TO_SECTION_INSTRUCTION);
        }

        int dirIdx = parts.length == 3 ? 1 : 0;
        int nameIdx = parts.length == 3 ? 2 : 1;

        dirName = parts[dirIdx];

        int idxOfAnchorSep = parts[nameIdx].indexOf('#');

        fileNameWithoutExtension = FilePathUtils.fileNameWithoutExtension(idxOfAnchorSep == -1 ? parts[nameIdx] : parts[nameIdx].substring(0, idxOfAnchorSep));
        anchorId = idxOfAnchorSep == -1 ? "" : parts[nameIdx].substring(idxOfAnchorSep + 1);

        return true;
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

    public boolean isFilePathBased() {
        return !tocItemFilePath.isEmpty();
    }

    public String getTocItemFilePath() {
        return tocItemFilePath;
    }

    public void setResolvedToDirNameAndFileName(String dirName, String fileNameWithoutExtension) {
        this.tocItemFilePath = "";
        this.dirName = dirName;
        this.fileNameWithoutExtension = fileNameWithoutExtension;
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
