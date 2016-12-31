package com.twosigma.documentation.html;

import com.twosigma.documentation.structure.DocMeta;

/**
 * @author mykola
 */
public class HtmlNavBar {
    private DocMeta docMeta;
    private String tocRelativeHref;
    private boolean isTocActive;

    public HtmlNavBar(DocMeta docMeta, String tocRelativeHref) {
        this.docMeta = docMeta;
        this.tocRelativeHref = tocRelativeHref;
    }

    public void setTocAsActive() {
        this.isTocActive = true;
    }

    public String render(HtmlRenderContext renderContext) {
        String brand = "<strong>" + docMeta.getTitle() + "</strong> " + docMeta.getType();

        return "<nav class=\"navbar navbar-default navbar-static-top\">\n" +
            "      <div class=\"container\">\n" +
            "        <div class=\"navbar-header\">\n" +
            "          <a class=\"navbar-brand\" href=\"#\">" + docMeta.getLogo().generateImageLink(renderContext, 48) + brand + "</a>\n" +
            "        </div>\n" +
            "        <div id=\"navbar\" class=\"navbar-collapse collapse\">\n" +
            "          <ul class=\"nav navbar-nav\">\n" +
            "            <li class=\"" + (isTocActive ? "active" : "") + "\"><a href=\"" + tocRelativeHref +"\">Table Of Contents</a></li>\n" +
            "          </ul>\n" +
            "          <ul class=\"nav navbar-nav navbar-right\">\n" +
            "              <span id=\"search-icon\" class=\"search-icon glyphicon glyphicon-search\"></span>\n" +
            "              <input id=\"search-box\"/>" +
            "          </ul>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "    </nav>";
    }
}
