package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.Deployer;
import com.twosigma.documentation.html.WebResource;
import com.twosigma.utils.ResourceUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class ReactJsBundle {
    private List<WebResource> resources;
    private WebResource react;
    private WebResource reactDom;
    private WebResource reactDomServer;

    public ReactJsBundle(String bundleResourceName) {
        List<String> textContents = ResourceUtils.textContents(bundleResourceName);

        System.out.println("----");
        resources = textContents.stream().flatMap(c -> Arrays.stream(c.split("\n")).
                map(WebResource::fromResource)).collect(toList());

        react = WebResource.fromResource("react.min.js");
        reactDom = WebResource.fromResource("react-dom.min.js");
        reactDomServer = WebResource.fromResource("react-dom-server.min.js");
    }

    public WebResource react() {
        return react;
    }

    public WebResource reactDom() {
        return reactDom;
    }

    public WebResource reactDomServer() {
        return reactDomServer;
    }

    public Stream<WebResource> javaScriptResources() {
        return resources.stream().filter(r -> r.getRelativePath().endsWith(".js"));
    }

    public Stream<WebResource> cssResources() {
        return resources.stream().filter(r -> r.getRelativePath().endsWith(".css"));
    }

    public void deploy(Deployer deployer) {
        resources.forEach(deployer::deploy);
        Stream.of(react, reactDom).forEach(deployer::deploy);
    }
}
