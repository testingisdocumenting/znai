package com.twosigma.documentation.html;

import com.twosigma.documentation.extensions.PluginsListener;
import com.twosigma.documentation.html.reactjs.NashornEngine;
import com.twosigma.documentation.html.reactjs.ReactJsBundle;
import com.twosigma.documentation.parser.Page;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;
import com.twosigma.utils.JsonUtils;

import java.util.LinkedHashMap;
import java.util.Map;

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

    public HtmlPage convert(TableOfContents toc, TocItem tocItem, Page page) {
        final HtmlPage htmlPage = new HtmlPage();
        htmlPage.setTitle(tocItem.getPageTitle());

        RenderSupplier createElementStatement = (rc) -> "React.createElement(docComponents.Page, " + JsonUtils.serializePrettyPrint(
            createPageProps(toc, tocItem, page, rc)) + ")";
        RenderSupplier reactServerRenderStatement = (rc) -> "ReactDOMServer.renderToString(" +
            createElementStatement.render(rc) + ");";

        htmlPage.addToBody((rc) -> "<div id=\"" + REACT_BLOCK_ID + "\">" +
            nashornEngine.eval(reactServerRenderStatement.render(rc)).toString() + "</div>");

        // TODO investigate: there were issues with syncing complex custom components
        htmlPage.addToJavaScript((rc) -> "ReactDOM.render(" + createElementStatement.render(rc) + ", " +
            "document.getElementById(\"" + REACT_BLOCK_ID + "\"));");

        htmlPage.addJavaScript(reactJsBundle.react());
        htmlPage.addJavaScript(reactJsBundle.reactDom());
        reactJsBundle.javaScriptResources().forEach(htmlPage::addJavaScript);
        reactJsBundle.cssResources().forEach(htmlPage::addCss);

        return htmlPage;
    }

    private Map<String, Object> createPageProps(final TableOfContents toc, final TocItem tocItem, final Page page, final HtmlRenderContext renderContext) {
        final Map<String, Object> pageAsMap = page.getDocElement().toMap();
        Map<String, Object> pageProps = new LinkedHashMap<>();
        pageProps.put("content", pageAsMap.get("content"));
        pageProps.put("docMeta", docMeta.toMap());
        pageProps.put("title", tocItem.getPageTitle());
        pageProps.put("type", "Page");
        pageProps.put("toc", toc.toListOfMaps());
        pageProps.put("renderContext", renderContext.toMap());

        System.out.println(JsonUtils.serializePrettyPrint(pageProps));

        return pageProps;
    }
}
