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

package com.twosigma.znai.html;

import com.twosigma.znai.structure.Page;
import com.twosigma.znai.structure.TocItem;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * represents props for ReactJs Doc Page component to render an entire documentation page
 */
public class DocPageReactProps {
    private final Map<String, ?> asMap;
    private final TocItem tocItem;
    private final Page page;

    public DocPageReactProps(TocItem tocItem, Page page) {
        this.tocItem = tocItem;
        this.page = page;
        this.asMap = buildMap();
    }

    public TocItem getTocItem() {
        return tocItem;
    }

    public Map<String, ?> toMap() {
        return asMap;
    }

    private Map<String, ?> buildMap() {
        Map<String, Object> pageProps = new LinkedHashMap<>();

        pageProps.put("type", "Page");
        pageProps.put("content", ((Map<String, ?>) page.getDocElement().toMap()).get("content"));
        pageProps.put("lastModifiedTime", page.getLastModifiedTime().toEpochMilli());
        pageProps.put("tocItem", tocItem.toMap());

        return pageProps;
    }
}
