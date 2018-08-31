package com.twosigma.documentation.html;

import com.twosigma.documentation.html.reactjs.HtmlReactJsPage;
import com.twosigma.documentation.html.reactjs.ReactJsBundle;
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
    private final ReactJsBundle reactJsBundle;

    public PageToHtmlPageConverter(DocMeta docMeta,
                                   ReactJsBundle reactJsBundle) {
        this.docMeta = docMeta;
        this.reactJsBundle = reactJsBundle;
    }

    public HtmlPageAndPageProps convert(TocItem tocItem,
                                        Page page,
                                        RenderSupplier mainBodySupplier,
                                        Footer footer) {
        String title = tocItem.isIndex() ?
                docMeta.getTitle() :
                docMeta.getTitle() + ": " + tocItem.getPageTitle();

        DocPageReactProps pageProps = new DocPageReactProps(tocItem, page);
        FooterProps footerProps = new FooterProps(footer);
        DocumentationReactProps docProps = new DocumentationReactProps(docMeta, pageProps, footerProps);

        HtmlReactJsPage reactJsPage = new HtmlReactJsPage(reactJsBundle);
        HtmlPage htmlPage = reactJsPage.create(title, "Documentation", docProps.toMap(), mainBodySupplier, "");

        WebSiteResourcesProviders.jsResources().forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.jsClientOnlyResources().forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.cssResources().forEach(htmlPage::addCss);
        WebSiteResourcesProviders.htmlResources().map(WebResource::getTextContent)
                .forEach(text -> htmlPage.addToBody(() -> text));

        return new HtmlPageAndPageProps(htmlPage, pageProps);
    }
}
