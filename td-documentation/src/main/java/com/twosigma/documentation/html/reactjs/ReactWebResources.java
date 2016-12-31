package com.twosigma.documentation.html.reactjs;

import java.nio.file.Paths;

import com.twosigma.documentation.html.WebResource;

/**
 * @author mykola
 */
public class ReactWebResources {
    public final static WebResource react = resource("react.path", "react.js");
    public final static WebResource reactDom = resource("react-dom.path", "react-dom.js");
    public final static WebResource reactDomServerSide = resource("react-dom.path", "react-dom-server.js");

    private static WebResource resource(String propName, String relativePath) {
        return null;
        // TODO reconcile
//        return WebResource.fromFileSystemWithRelativePath(
//            Paths.get(TSProperties.getStringProperty(propName)).resolve(relativePath),
//            relativePath);
    }
}
