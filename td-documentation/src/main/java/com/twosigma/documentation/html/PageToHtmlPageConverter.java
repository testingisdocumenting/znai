package com.twosigma.documentation.html;

import com.twosigma.documentation.extensions.PluginsListener;
import com.twosigma.documentation.html.reactjs.NashornEngine;
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
    private final NashornEngine nashornEngine;

    private final ReactJsBundle reactJsBundle;

    public PageToHtmlPageConverter(DocMeta docMeta, TableOfContents tableOfContents, ReactJsBundle reactJsBundle, PluginsListener pluginsListener) {
        this.docMeta = docMeta;
        this.nashornEngine = new NashornEngine();
        this.reactJsBundle = reactJsBundle;

        nashornEngine.loadLibrary(reactJsBundle.react());
        nashornEngine.loadLibrary(reactJsBundle.reactDomServer());
        reactJsBundle.javaScriptResources().forEach(nashornEngine::loadLibrary);
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

        htmlPage.addJavaScript(reactJsBundle.react());
        htmlPage.addJavaScript(reactJsBundle.reactDom());
        reactJsBundle.javaScriptResources().forEach(htmlPage::addJavaScript);
        reactJsBundle.cssResources().forEach(htmlPage::addCss);

        return new HtmlPageAndPageProps(htmlPage, pageProps);
    }

    private Object nashornEval(String renderStatement) {
        try {
            return nashornEngine.eval(renderStatement);
        } catch (Exception e) {
            throw new RuntimeException("failed to eval:\n" + renderStatement, e);
        }
    }

}
