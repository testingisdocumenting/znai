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

public class DocUrl {
    private static final String LINK_TO_SECTION_INSTRUCTION = "To refer to a section of this document use" +
            " dir-name/file-name[#page-section-id]";

    private String dirName = "";
    private String fileName = "";
    private String anchorId = "";

    private String url;

    private boolean isGlobalUrl;
    private boolean isIndexUrl;

    public static DocUrl indexUrl() {
        return new DocUrl(true);
    }

    private DocUrl(boolean isIndexUrl) {
        this.isIndexUrl = isIndexUrl;
    }

    public DocUrl(String dirName, String fileName, String anchorId) {
        this.dirName = dirName;
        this.fileName = fileName;
        this.anchorId = anchorId;
    }

    public DocUrl(String url) {
        this.url = url;
        if (url.startsWith("http") || url.startsWith("file") || url.startsWith("mailto")) {
            isGlobalUrl = true;
            return;
        }

        if (url.equals("/")) {
            isIndexUrl = true;
            return;
        }

        if (url.startsWith("..")) {
            throw new IllegalArgumentException("Do not use .. based urls: " + url + ". " + LINK_TO_SECTION_INSTRUCTION);
        }

        String[] parts = url.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Unexpected url pattern: " + url + ". " + LINK_TO_SECTION_INSTRUCTION);
        }

        dirName = parts[0];

        int idxOfAnchorSep = parts[1].indexOf('#');

        fileName = idxOfAnchorSep == -1 ? parts[1] : parts[1].substring(0, idxOfAnchorSep);
        anchorId = idxOfAnchorSep == -1 ? "" : parts[1].substring(idxOfAnchorSep + 1);
    }

    public boolean isIndexUrl() {
        return isIndexUrl;
    }

    public boolean isGlobalUrl() {
        return isGlobalUrl;
    }

    public String getDirName() {
        return dirName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public String getUrl() {
        return url;
    }
}
