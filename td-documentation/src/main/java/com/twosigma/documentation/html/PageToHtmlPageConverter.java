package com.twosigma.documentation.html;

import com.twosigma.documentation.extensions.PluginsListener;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.nashorn.NashornEngine;
import com.twosigma.documentation.html.reactjs.ReactJsBundle;
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

    public PageToHtmlPageConverter(DocMeta docMeta, TableOfContents tableOfContents, ReactJsNashornEngine reactJsNashornEngine, PluginsListener pluginsListener) {
        this.docMeta = docMeta;
        this.reactJsNashornEngine = reactJsNashornEngine;
    }

    public HtmlPageAndPageProps convert(TableOfContents toc, TocItem tocItem, Page page, HtmlRenderContext renderContext) {
        final HtmlPage htmlPage = new HtmlPage();
        htmlPage.setTitle(tocItem.getPageTitle());

        PageProps pageProps = new PageProps(tocItem, page, renderContext);
        DocumentationProps docProps = new DocumentationProps(docMeta, toc, pageProps);

        // TODO reconsider the whole rc business below
        RenderSupplier createElementStatement = (rc) -> "React.createElement(Documentation, " + JsonUtils.serializePrettyPrint(
                    docProps.toMap()) + ")";

        RenderSupplier reactServerRenderStatement = (rc) -> "ReactDOMServer.renderToString(" +
            createElementStatement.render(rc) + ");";

        htmlPage.addToBody((rc) -> {
            String renderStatement = reactServerRenderStatement.render(rc);
            return "<div id=\"" + REACT_BLOCK_ID + "\">" + nashornEval(renderStatement).toString() + "</div>";
        });

        // TODO investigate: there were issues with syncing complex custom components
        htmlPage.addToJavaScript((rc) -> "ReactDOM.render(" + createElementStatement.render(rc) + ", " +
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
