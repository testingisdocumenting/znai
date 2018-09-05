package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.Deployer;
import com.twosigma.documentation.web.WebResource;
import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.ResourceUtils;

import java.util.List;
import java.util.Map;
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
    private final WebResource bootstrapCss;

    private final List<WebResource> fonts;

    public ReactJsBundle() {
        mainJs = WebResource.fromResource("static/main.js");
        mainCss = WebResource.fromResource("static/main.css");
        bootstrapCss = WebResource.fromResource("static/css/bootstrap.min.css");
        react = WebResource.fromResource("static/react.min.js");
        reactDom = WebResource.fromResource("static/react-dom.min.js");

        Stream<WebResource> glyphIcons = Stream.of("eot", "svg", "ttf", "woff", "woff2")
                .map(ext -> "static/fonts/glyphicons-halflings-regular." + ext)
                .map(WebResource::fromResource);

        Map<String, Object> manifest = loadManifest();

        Stream<WebResource> katexFonts = Stream.of("ttf", "woff", "woff2")
                .flatMap(ext -> Stream.of(
                        "KaTeX_Main-Bold." + ext,
                        "KaTeX_Main-BoldItalic." + ext,
                        "KaTeX_Main-Italic." + ext,
                        "KaTeX_Main-Regular." + ext,
                        "KaTeX_Math-BoldItalic." + ext,
                        "KaTeX_Math-Italic." + ext)
                        .map(name -> WebResource.fromResource(manifest.get("static/media/" + name).toString())));

        fonts = Stream.concat(glyphIcons, katexFonts).collect(toList());
    }

    public Stream<WebResource> clientJavaScripts() {
        return Stream.of(react, reactDom, mainJs);
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

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadManifest() {
        String assetManifest = ResourceUtils.textContent("static/asset-manifest.json");
        return (Map<String, Object>) JsonUtils.deserialize(assetManifest);
    }
}
