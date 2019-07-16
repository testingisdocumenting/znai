package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.HtmlPage;
import com.twosigma.documentation.html.RenderSupplier;
import com.twosigma.utils.JsonUtils;

import java.util.Map;

public class HtmlReactJsPage {
    private static final String REACT_BLOCK_ID = "mdoc";

    private final ReactJsBundle reactJsBundle;

    public HtmlReactJsPage(ReactJsBundle reactJsBundle) {
        this.reactJsBundle = reactJsBundle;
    }

    public HtmlPage create(String title,
                           String component,
                           Map<String, ?> props,
                           RenderSupplier mainBodySupplier,
                           String favIconCustomPath) {
        HtmlPage htmlPage = new HtmlPage(favIconCustomPath);
        htmlPage.setTitle(title);

        RenderSupplier createElementStatement = () -> "React.createElement(" + component + ", " +
                JsonUtils.serializePrettyPrint(props) + ")";

        htmlPage.addToBody(() -> "<div id=\"" + REACT_BLOCK_ID + "\">" +
                mainBodySupplier.render() + "</div>");

        htmlPage.addToJavaScript(() -> "document.getElementById('" + REACT_BLOCK_ID + "').innerHTML = '';\n" +
                "ReactDOM.render(" + createElementStatement.render() + ", " +
                "document.getElementById(\"" + REACT_BLOCK_ID + "\"));");

        reactJsBundle.clientJavaScripts().forEach(htmlPage::addJavaScript);
        reactJsBundle.clientCssResources().forEach(htmlPage::addCss);

        return htmlPage;
    }
}
