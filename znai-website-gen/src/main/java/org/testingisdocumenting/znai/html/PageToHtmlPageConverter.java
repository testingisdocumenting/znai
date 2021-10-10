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

import org.testingisdocumenting.znai.html.reactjs.HtmlReactJsPage;
import org.testingisdocumenting.znai.html.reactjs.ReactJsBundle;
import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.structure.Page;
import org.testingisdocumenting.znai.structure.TocItem;

public class PageToHtmlPageConverter {
    private final DocMeta docMeta;
    private final ReactJsBundle reactJsBundle;

    public PageToHtmlPageConverter(DocMeta docMeta,
                                   ReactJsBundle reactJsBundle) {
        this.docMeta = docMeta;
        this.reactJsBundle = reactJsBundle;
    }

    public HtmlPageAndPageProps convert(TocItem tocItem,
                                        Page page,
                                        RenderSupplier mainBodySupplier) {
        String title = tocItem.isIndex() ?
                docMeta.getTitle() :
                docMeta.getTitle() + ": " + tocItem.getPageTitle();

        DocPageReactProps pageProps = new DocPageReactProps(tocItem, page);
        DocumentationReactProps docProps = new DocumentationReactProps(docMeta, pageProps);

        HtmlReactJsPage reactJsPage = new HtmlReactJsPage(reactJsBundle);
        HtmlPage htmlPage = reactJsPage.create(title, "Documentation", docProps.toMap(), mainBodySupplier, "");

        return new HtmlPageAndPageProps(htmlPage, pageProps);
    }
}
