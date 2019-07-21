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

import com.twosigma.znai.structure.DocMeta;

import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentationReactProps {
    private final DocMeta docMeta;
    private final DocPageReactProps pageProps;
    private FooterProps footerProps;

    public DocumentationReactProps(DocMeta docMeta, DocPageReactProps pageProps, FooterProps footerProps) {
        this.docMeta = docMeta;
        this.pageProps = pageProps;
        this.footerProps = footerProps;
    }

    public Map<String, ?> toMap() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("docMeta", docMeta.toMap());
        map.put("page", pageProps.toMap());
        map.put("footer", footerProps.toMap());

        return map;
    }
}
