package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.Deployer;
import com.twosigma.documentation.html.WebResource;
import com.twosigma.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class ReactJsBundle {
    private WebResource mainJs;
    private WebResource mainCss;
    private WebResource react;
    private WebResource reactDom;
    private WebResource reactDomServer;
    private WebResource bootstrapCss;

    private List<WebResource> fonts;

    public ReactJsBundle() {
        mainJs = WebResource.fromResource("static/main.js");
        mainCss = WebResource.fromResource("static/main.css");
        bootstrapCss = WebResource.fromResource("static/css/bootstrap.min.css");
        react = WebResource.fromResource("static/react.min.js");
        reactDom = WebResource.fromResource("static/react-dom.min.js");
        reactDomServer = WebResource.fromResource("react-dom-server.min.js");

        fonts = Stream.of("eot", "svg", "ttf", "woff", "woff2").map(ext -> "static/fonts/glyphicons-halflings-regular." + ext).
                map(WebResource::fromResource).collect(toList());
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
