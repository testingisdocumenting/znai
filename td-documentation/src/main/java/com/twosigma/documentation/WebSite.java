package com.twosigma.documentation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import com.twosigma.documentation.extensions.IncludeContext;
import com.twosigma.documentation.extensions.IncludePlugins;
import com.twosigma.documentation.extensions.PluginsListener;
import com.twosigma.documentation.html.*;
import com.twosigma.documentation.html.reactjs.ReactJsBundle;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.nashorn.NashornEngine;
import com.twosigma.documentation.parser.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.Page;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;
import com.twosigma.utils.JsonUtils;

import static com.twosigma.utils.FileUtils.fileTextContent;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class WebSite {
    private PageToHtmlPageConverter pageToHtmlPageConverter;
    private MarkupParser markupParser;
    private final Deployer deployer;
    private final DocMeta docMeta;
    private Configuration cfg;

    private Map<TocItem, Page> pageByTocItem;
    private TableOfContents toc;
    private List<PageProps> allPagesProps;
    private List<WebResource> registeredExtraJavaScripts;
    private List<WebResource> extraJavaScripts;
    private WebResource lunrIndexJavaScript;
    private WebResource allPagesJavaScript;
    private final ReactJsNashornEngine reactJsNashornEngine;

    private WebSite(Configuration cfg) {
        this.cfg = cfg;
        this.deployer = new Deployer(cfg.deployPath);
        this.docMeta = new DocMeta();
        this.registeredExtraJavaScripts = cfg.registeredExtraJavaScripts;
        this.reactJsNashornEngine = new ReactJsNashornEngine();

        docMeta.setTitle(cfg.title);
        docMeta.setType(cfg.type);
        docMeta.setLogo(WebResource.withRelativePath(cfg.logoRelativePath));
        lunrIndexJavaScript = WebResource.withRelativePath("index.js");
        allPagesJavaScript = WebResource.withRelativePath("pages.js");

        reset();
    }

    public static Configuration withToc(Path path) {
        final Configuration configuration = new Configuration();
        configuration.setTocPath(path);

        return configuration;
    }

    public void regenerate() {
        reset();
        generate();
    }

    public Path getDeployRoot() {
        return cfg.deployPath;
    }

    public Path getTocPath() {
        return cfg.tocPath;
    }

    public void generate() {
        deployResources();
        createToc();
        parseMarkups();
//        generateSearchIndex();
        generatePages();
    }

    public TocItem tocItemByPath(Path path) {
        return toc.getTocItems().stream().filter(ti ->
            path.toAbsolutePath().getParent().getFileName().toString().equals(ti.getDirName()) &&
                path.getFileName().toString().equals(ti.getFileNameWithoutExtension() + ".md")).findFirst().orElse(null);
    }

    public void regeneratePage(TocItem tocItem) {
        parseMarkup(tocItem);
        final Page page = pageByTocItem.get(tocItem);
        generatePage(tocItem, page);
//        generateSearchIndex();
    }

    private void reset() {
        pageToHtmlPageConverter = new PageToHtmlPageConverter(docMeta, toc, reactJsNashornEngine, cfg.pluginsListener);
        markupParser = new MarkdownParser();
        pageByTocItem = new LinkedHashMap<>();
        allPagesProps = new ArrayList<>();
        extraJavaScripts = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScripts.add(lunrIndexJavaScript);
        extraJavaScripts.add(allPagesJavaScript);
    }

    private void deployResources() {
        reactJsNashornEngine.getReactJsBundle().deploy(deployer);
        cfg.webResources.forEach(deployer::deploy);
    }

    private void createToc() {
        toc = TableOfContents.fromNestedText(fileTextContent(cfg.tocPath));
    }

    private void parseMarkups() {
        toc.getTocItems().forEach(this::parseMarkup);
    }

    private void parseMarkup(final TocItem tocItem) {
        try {
            Path markupPath = markupPath(tocItem);

            resetPlugins(markupPath);

            final Page page = new Page(markupParser.parse(fileTextContent(markupPath)));
            pageByTocItem.put(tocItem, page);
        } catch(Exception e) {
            throw new RuntimeException("error during parsing of " + tocItem.getFileNameWithoutExtension(), e);
        }
    }

    private Path markupPath(final TocItem tocItem) {
        return cfg.tocPath.toAbsolutePath().getParent().resolve(
            tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension() + ".md"); // TODO extension auto figure out
    }

    private void resetPlugins(final Path markdownPath) {
        final IncludeContext context = new IncludeContext(markdownPath);
        IncludePlugins.reset(context);
        if (cfg.pluginsListener != null) {
            cfg.pluginsListener.onReset(context);
        }
    }

//    private void generateSearchIndex() {
//        lunrIndex = new LunrIndex();
//
//        forEachPage((tocItem, mdPage) -> mdPage.getNode().accept(new MdNodeSearchVisitor(lunrIndex, tocItem)));
//        deployer.deploy(lunrIndexJavaScript.getRelativePath(), lunrIndex.generateJavaScript());
//    }

    private void generatePages() {
        forEachPage(this::generatePage);
        buildJsonOfAllPages();
    }

    private void buildJsonOfAllPages() {
        List<Map<String, ?>> listOfMaps = this.allPagesProps.stream().map(PageProps::toMap).collect(toList());
        String json = JsonUtils.serialize(listOfMaps);
        String js = "allPagesData = " + json + ";";

        deployer.deploy("pages.js", js);
    }

    private void generatePage(final TocItem tocItem, final Page page) {
        try {
            resetPlugins(markupPath(tocItem)); // TODO reset at render phase only?

            // we are inside directory, so nest level is 1 for all the static resources
            HtmlRenderContext renderContext = HtmlRenderContext.nested(1);

            final HtmlPageAndPageProps htmlAndProps = pageToHtmlPageConverter.convert(toc, tocItem, page, renderContext);

            allPagesProps.add(htmlAndProps.getProps());
            extraJavaScripts.forEach(htmlAndProps.getHtmlPage()::addJavaScript);

            final String html = htmlAndProps.getHtmlPage().render(renderContext);
            deployer.deploy(Paths.get(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension() + ".html"), html);
        } catch (Exception e) {
            throw new RuntimeException("Error during rendering of " + tocItem.getFileNameWithoutExtension(), e);
        }
    }

//    private void generateTocPage() {
//        final TocHtmlPage tocHtmlPage = new TocHtmlPage(docMeta, pageByTocItem);
//        extraJavaScripts.forEach(tocHtmlPage::addJavaScript);
//
//        // we are outside any directory on a zero level, so all the links to css/js/img should be normal links without ".."
//        final String html = tocHtmlPage.render(HtmlRenderContext.nested(0));
//        deployer.deploy(Paths.get("index.html"), html);
//    }

    private void forEachPage(PageConsumer consumer) {
        toc.getTocItems().forEach(tocItem -> {
            final Page page = pageByTocItem.get(tocItem);
            consumer.consume(tocItem, page);
        });
    }

    private interface PageConsumer {
        void consume(TocItem tocItem, Page page);
    }

    public static class Configuration {
        private Path deployPath;
        private Path tocPath;
        private List<Path> webResources;
        private String title;
        private String type;
        private String logoRelativePath;
        private PluginsListener pluginsListener;
        public List<WebResource> registeredExtraJavaScripts;

        private Configuration() {
            webResources = new ArrayList<>();
            registeredExtraJavaScripts = new ArrayList<>();
        }

        public Configuration setTocPath(Path path) {
            tocPath = path.toAbsolutePath();
            return this;
        }

        public Configuration withWebResources(Path... paths) {
            Arrays.stream(paths).forEach(p -> webResources.add(p));
            return this;
        }

        public Configuration withExtraJavaScripts(WebResource... webResources) {
            this.registeredExtraJavaScripts.addAll(Arrays.asList(webResources));
            return this;
        }

        public Configuration withTitle(String title) {
            this.title = title;
            return this;
        }

        public Configuration withType(String type) {
            this.type = type;
            return this;
        }

        public Configuration withPluginListener(PluginsListener pluginsListener) {
            this.pluginsListener = pluginsListener;
            return this;
        }

        public Configuration withMetaFromJsonFile(Path path) {
            final String json = fileTextContent(path);
            final Gson gson = new Gson();
            final Map map = gson.fromJson(json, Map.class);

            withTitle(map.get("title").toString());
            withType(map.get("type").toString());

            return this;
        }

        public Configuration withLogoRelativePath(String logoRelativePath) {
            this.logoRelativePath = logoRelativePath;
            return this;
        }

        public WebSite deployTo(Path path) {
            deployPath = path.toAbsolutePath();
            final WebSite webSite = new WebSite(this);
            webSite.generate();

            return webSite;
        }
    }
}
