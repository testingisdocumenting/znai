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

package com.twosigma.znai.website;

import com.twosigma.znai.html.HtmlPageAndPageProps;
import com.twosigma.znai.structure.TableOfContents;
import com.twosigma.znai.structure.TocItem;

import java.util.List;

public class TocAddedAndRemovedPages {
    private final TableOfContents tableOfContents;
    private final List<HtmlPageAndPageProps> addedPagesProps;
    private List<TocItem> removedTocItems;

    public TocAddedAndRemovedPages(TableOfContents tableOfContents,
                                   List<HtmlPageAndPageProps> addedPagesProps,
                                   List<TocItem> removedTocItems) {
        this.tableOfContents = tableOfContents;
        this.addedPagesProps = addedPagesProps;
        this.removedTocItems = removedTocItems;
    }

    public TableOfContents getTableOfContents() {
        return tableOfContents;
    }

    public List<HtmlPageAndPageProps> getAddedPagesProps() {
        return addedPagesProps;
    }

    public List<TocItem> getRemovedTocItems() {
        return removedTocItems;
    }
}
