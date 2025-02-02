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

package org.testingisdocumenting.znai.search;

import org.testingisdocumenting.znai.extensions.file.SnippetCleaner;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchText {
    private SearchScore score;
    private String text;

    public SearchText() {
    }

    public SearchText(SearchScore score, String text) {
        this.score = score;
        this.text = removeNonReadableSymbols(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SearchScore getScore() {
        return score;
    }

    public void setScore(SearchScore score) {
        this.score = score;
    }

    private static String removeNonReadableSymbols(String text) {
        if (text == null) {
            return null;
        }

        return SnippetCleaner.removeNonPrintable(text);
    }

    @Override
    public String toString() {
        return "SearchText{" +
                "score=" + score +
                ", text='" + text + '\'' +
                '}';
    }
}
