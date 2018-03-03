package com.twosigma.documentation.html;

/**
 * @author mykola
 */
public class HtmlPageAndPageProps {
    private HtmlPage htmlPage;
    private PageReactProps props;

    public HtmlPageAndPageProps(HtmlPage htmlPage, PageReactProps props) {
        this.htmlPage = htmlPage;
        this.props = props;
    }

    public HtmlPage getHtmlPage() {
        return htmlPage;
    }

    public PageReactProps getProps() {
        return props;
    }
}
