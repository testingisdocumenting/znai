package com.twosigma.documentation.html;

import com.twosigma.documentation.html.reactjs.HtmlReactJsPage;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.structure.*;
import com.twosigma.documentation.website.WebSiteExtensions;

/**
 * @author mykola
 */
public class PageToHtmlPageConverter {
    private final DocMeta docMeta;
    private WebSiteExtensions webSiteExtensions;
    private final ReactJsNashornEngine reactJsNashornEngine;

    public PageToHtmlPageConverter(DocMeta docMeta,
                                   WebSiteExtensions webSiteExtensions,
                                   ReactJsNashornEngine reactJsNashornEngine) {
        this.docMeta = docMeta;
        this.webSiteExtensions = webSiteExtensions;
        this.reactJsNashornEngine = reactJsNashornEngine;
    }

    public HtmlPageAndPageProps convert(TocItem tocItem, Page page, Footer footer) {
        String title = tocItem.isIndex() ?
                docMeta.getTitle() :
                docMeta.getTitle() + ": " + tocItem.getPageTitle();

        DocPageReactProps pageProps = new DocPageReactProps(tocItem, page);
        FooterProps footerProps = new FooterProps(footer);
        DocumentationReactProps docProps = new DocumentationReactProps(docMeta, pageProps, footerProps);

        HtmlReactJsPage reactJsPage = new HtmlReactJsPage(reactJsNashornEngine);
        HtmlPage htmlPage = reactJsPage.createWithServerSideRendering(title, "Documentation", docProps.toMap());

        webSiteExtensions.getJsResources().forEach(htmlPage::addJavaScript);
        webSiteExtensions.getCssResources().forEach(htmlPage::addCss);

        return new HtmlPageAndPageProps(htmlPage, pageProps);
    }
}
