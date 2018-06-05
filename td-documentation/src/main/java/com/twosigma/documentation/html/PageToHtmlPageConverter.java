package com.twosigma.documentation.html;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.html.reactjs.HtmlReactJsPage;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.Footer;
import com.twosigma.documentation.structure.Page;
import com.twosigma.documentation.structure.TocItem;
import com.twosigma.documentation.web.WebResource;
import com.twosigma.documentation.web.extensions.WebSiteResourcesProviders;

/**
 * @author mykola
 */
public class PageToHtmlPageConverter {
    private final DocMeta docMeta;
    private final ResourcesResolver resourcesResolver;
    private final ReactJsNashornEngine reactJsNashornEngine;

    public PageToHtmlPageConverter(DocMeta docMeta,
                                   ResourcesResolver resourcesResolver,
                                   ReactJsNashornEngine reactJsNashornEngine) {
        this.docMeta = docMeta;
        this.resourcesResolver = resourcesResolver;
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

        WebSiteResourcesProviders.jsResources(resourcesResolver).forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.jsClientOnlyResources(resourcesResolver).forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.cssResources(resourcesResolver).forEach(htmlPage::addCss);
        WebSiteResourcesProviders.htmlResources(resourcesResolver).map(WebResource::getTextContent)
                .forEach(text -> htmlPage.addToBody(() -> text));

        return new HtmlPageAndPageProps(htmlPage, pageProps);
    }
}
