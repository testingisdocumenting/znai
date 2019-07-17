package com.twosigma.znai.html;

public class HtmlPageAndPageProps {
    private HtmlPage htmlPage;
    private DocPageReactProps props;

    public HtmlPageAndPageProps(HtmlPage htmlPage, DocPageReactProps props) {
        this.htmlPage = htmlPage;
        this.props = props;
    }

    public HtmlPage getHtmlPage() {
        return htmlPage;
    }

    public DocPageReactProps getProps() {
        return props;
    }
}
