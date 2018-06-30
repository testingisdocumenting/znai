package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.HtmlPage;
import com.twosigma.documentation.html.RenderSupplier;
import com.twosigma.utils.JsonUtils;

import java.util.Map;

public class HtmlReactJsPage {
    private static final String REACT_BLOCK_ID = "mdoc";

    private final ReactJsNashornEngine reactJsNashornEngine;

    public HtmlReactJsPage(ReactJsNashornEngine reactJsNashornEngine) {
        this.reactJsNashornEngine = reactJsNashornEngine;
    }

    public HtmlPage createWithServerSideRendering(String title, String component, Map<String, ?> props, String favIconCustomPath) {
        return create(title, component, props, true, favIconCustomPath);
    }

    public HtmlPage createWithClientSideOnly(String title, String component, Map<String, ?> props, String favIconCustomPath) {
        return create(title, component, props, false, favIconCustomPath);
    }

    private HtmlPage create(String title, String component, Map<String, ?> props, boolean serverSideRendering, String favIconCustomPath) {
        HtmlPage htmlPage = new HtmlPage(favIconCustomPath);
        htmlPage.setTitle(title);

        RenderSupplier createElementStatement = () -> "React.createElement(" + component + ", " +
                JsonUtils.serializePrettyPrint(props) + ")";

        RenderSupplier reactServerRenderStatement = () -> "ReactDOMServer.renderToString(" +
                createElementStatement.render() + ");";

        htmlPage.addToBody(() -> {
            String preRendered = serverSideRendering ? nashornEval(reactServerRenderStatement.render()).toString() : "";
            return "<div id=\"" + REACT_BLOCK_ID + "\">" + preRendered + "</div>";
        });

        htmlPage.addToJavaScript(() -> "document.getElementById('"+ REACT_BLOCK_ID + "').innerHTML = '';\n" +
                "ReactDOM.render(" + createElementStatement.render() + ", " +
                "document.getElementById(\"" + REACT_BLOCK_ID + "\"));");

        ReactJsBundle jsBundle = reactJsNashornEngine.getReactJsBundle();

        jsBundle.clientJavaScripts().forEach(htmlPage::addJavaScript);
        jsBundle.clientCssResources().forEach(htmlPage::addCss);

        return htmlPage;
    }

    private Object nashornEval(String renderStatement) {
        try {
            return reactJsNashornEngine.getNashornEngine().eval(renderStatement);
        } catch (Exception e) {
            throw new RuntimeException("failed to eval:\n" + renderStatement, e);
        }
    }
}
