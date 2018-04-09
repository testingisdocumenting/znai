package com.twosigma.documentation.html;

import com.twosigma.documentation.html.reactjs.ReactJsBundle;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.structure.*;
import com.twosigma.documentation.website.WebSiteExtensions;
import com.twosigma.utils.JsonUtils;

/**
 * @author mykola
 */
public class PageToHtmlPageConverter {
    private static final String REACT_BLOCK_ID = "webdoc";

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
        final HtmlPage htmlPage = new HtmlPage();
        htmlPage.setTitle(tocItem.isIndex() ?
                docMeta.getTitle():
                docMeta.getTitle() + ": " + tocItem.getPageTitle());

        PageReactProps pageProps = new PageReactProps(tocItem, page);
        FooterProps footerProps = new FooterProps(footer);
        DocumentationReactProps docProps = new DocumentationReactProps(docMeta, pageProps, footerProps);

        RenderSupplier createElementStatement = () -> "React.createElement(Documentation, " + JsonUtils.serializePrettyPrint(
                    docProps.toMap()) + ")";

        RenderSupplier reactServerRenderStatement = () -> "ReactDOMServer.renderToString(" +
                createElementStatement.render() + ");";

        htmlPage.addToBody(() -> {
            String renderStatement = reactServerRenderStatement.render();
            return "<div id=\"" + REACT_BLOCK_ID + "\">" + nashornEval(renderStatement).toString() + "</div>";
        });

        htmlPage.addToJavaScript(() -> "ReactDOM.render(" + createElementStatement.render() + ", " +
            "document.getElementById(\"" + REACT_BLOCK_ID + "\"));");

        ReactJsBundle jsBundle = reactJsNashornEngine.getReactJsBundle();

        jsBundle.clientJavaScripts().forEach(htmlPage::addJavaScript);
        webSiteExtensions.getJsResources().forEach(htmlPage::addJavaScript);

        jsBundle.clientCssResources().forEach(htmlPage::addCss);
        webSiteExtensions.getCssResources().forEach(htmlPage::addCss);

        return new HtmlPageAndPageProps(htmlPage, pageProps);
    }

    private Object nashornEval(String renderStatement) {
        try {
            return reactJsNashornEngine.getNashornEngine().eval(renderStatement);
        } catch (Exception e) {
            throw new RuntimeException("failed to eval:\n" + renderStatement, e);
        }
    }
}
