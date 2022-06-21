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

package org.testingisdocumenting.znai.html;

import org.testingisdocumenting.znai.structure.Page;
import org.testingisdocumenting.znai.structure.TocItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        pageProps.put("content", exerciseSuppliers(((Map<String, ?>) page.getDocElement().toMap()).get("content")));
        pageProps.put("lastModifiedTime", page.getLastModifiedTime().toEpochMilli());
        pageProps.put("tocItem", tocItem.toMap());

        return pageProps;
    }

    /**
     * values inside a map could be lazy evaluated Suppliers and we need to exercise them before rendering the page
     * @see org.testingisdocumenting.znai.extensions.toc.PageTocIncludePlugin
     * @param content content with potential suppliers
     * @return content with exercised suppliers
     */
    @SuppressWarnings("unchecked")
    private static Object exerciseSuppliers(Object content) {
        if (content instanceof Supplier) {
            return ((Supplier<?>) content).get();
        }

        if (content instanceof Map) {
            return exerciseMapSuppliers((Map<String, ?>) content);
        }

        if (content instanceof List) {
            return exerciseListSuppliers((List<?>) content);
        }

        return content;
    }

    private static Map<String, ?> exerciseMapSuppliers(Map<String, ?> content) {
        return content.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> exerciseSuppliers(e.getValue()),
                        (a, b) -> b,
                        LinkedHashMap::new));
    }

    private static List<?> exerciseListSuppliers(List<?> content) {
        return content.stream().map(DocPageReactProps::exerciseSuppliers).collect(Collectors.toList());
    }
}
