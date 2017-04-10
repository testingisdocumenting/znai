package com.twosigma.documentation.html;

import com.twosigma.documentation.html.reactjs.ReactJsBundle;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.parser.Page;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;
import com.twosigma.utils.JsonUtils;

/**
 * @author mykola
 */
public class PageToHtmlPageConverter {
    private static final String REACT_BLOCK_ID = "webdoc";

    private final DocMeta docMeta;
    private final ReactJsNashornEngine reactJsNashornEngine;

    public PageToHtmlPageConverter(DocMeta docMeta, TableOfContents tableOfContents, ReactJsNashornEngine reactJsNashornEngine) {
        this.docMeta = docMeta;
        this.reactJsNashornEngine = reactJsNashornEngine;
    }

    public HtmlPageAndPageProps convert(TableOfContents toc, TocItem tocItem, Page page) {
        final HtmlPage htmlPage = new HtmlPage();
        htmlPage.setTitle(tocItem.isIndex() ?
                docMeta.getTitle():
                docMeta.getTitle() + ": " + tocItem.getPageTitle());

        PageProps pageProps = new PageProps(tocItem, page);
        DocumentationProps docProps = new DocumentationProps(docMeta, toc, pageProps);

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
        jsBundle.clientCssResources().forEach(htmlPage::addCss);

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
