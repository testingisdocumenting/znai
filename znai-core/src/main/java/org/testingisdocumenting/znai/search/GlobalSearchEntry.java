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

package org.testingisdocumenting.znai.search;

import java.util.Objects;

/**
 * global search entry
 */
public class GlobalSearchEntry {
    private String url;
    private String fullTitle;
    private SearchText text;

    public GlobalSearchEntry() {
    }

    public GlobalSearchEntry(String url, String fullTitle, String text) {
        this.url = url;
        this.fullTitle = fullTitle;
        this.text = SearchScore.STANDARD.text(text);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public SearchText getText() {
        return text;
    }

    public void setText(SearchText text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GlobalSearchEntry that = (GlobalSearchEntry) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(fullTitle, that.fullTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, fullTitle);
    }
}
