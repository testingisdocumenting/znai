package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.Deployer;
import com.twosigma.documentation.web.WebResource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class ReactJsBundle {
    private final WebResource mainJs;
    private final WebResource mainCss;
    private final WebResource react;
    private final WebResource reactDom;
    private final WebResource reactDomServer;
    private final WebResource bootstrapCss;

    private final List<WebResource> fonts;

    public ReactJsBundle() {
        mainJs = WebResource.fromResource("static/main.js");
        mainCss = WebResource.fromResource("static/main.css");
        bootstrapCss = WebResource.fromResource("static/css/bootstrap.min.css");
        react = WebResource.fromResource("static/react.min.js");
        reactDom = WebResource.fromResource("static/react-dom.min.js");
        reactDomServer = WebResource.fromResource("react-dom-server.min.js");

        Stream<WebResource> glyphIcons = Stream.of("eot", "svg", "ttf", "woff", "woff2")
                .map(ext -> "static/fonts/glyphicons-halflings-regular." + ext)
                .map(WebResource::fromResource);

        fonts = glyphIcons.collect(toList());
    }

    public Stream<WebResource> clientJavaScripts() {
        return Stream.of(react, reactDom, mainJs);
    }

    public Stream<WebResource> serverJavaScripts() {
        return Stream.of(react, reactDomServer, mainJs);
    }

    public Stream<WebResource> clientCssResources() {
        return Stream.of(bootstrapCss, mainCss);
    }

    public WebResource react() {
        return react;
    }

    public void deploy(Deployer deployer) {
        Stream.concat(Stream.concat(clientJavaScripts(), clientCssResources()),
                fonts.stream()).forEach(deployer::deploy);
    }
}
