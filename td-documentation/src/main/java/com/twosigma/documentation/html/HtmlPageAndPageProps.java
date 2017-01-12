package com.twosigma.documentation.html;

/**
 * @author mykola
 */
public class HtmlPageAndPageProps {
    private HtmlPage htmlPage;
    private PageProps props;

    public HtmlPageAndPageProps(HtmlPage htmlPage, PageProps props) {
        this.htmlPage = htmlPage;
        this.props = props;
    }

    public HtmlPage getHtmlPage() {
        return htmlPage;
    }

    public PageProps getProps() {
        return props;
    }
}
