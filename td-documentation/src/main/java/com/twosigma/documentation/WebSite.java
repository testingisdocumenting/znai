package com.twosigma.documentation;

import com.google.gson.Gson;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludePlugins;
import com.twosigma.documentation.extensions.PluginsListener;
import com.twosigma.documentation.extensions.include.IncludeResourcesResolver;
import com.twosigma.documentation.extensions.include.RelativeToFileAndRootResourceResolver;
import com.twosigma.documentation.html.*;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.parser.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.Page;
import com.twosigma.documentation.search.LunrIndexer;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    private final WebSiteComponentsRegistry componentsRegistry;
    private final RelativeToFileAndRootResourceResolver includeResourcesResolver;
    private final ReactJsNashornEngine reactJsNashornEngine;
    private final LunrIndexer lunrIndexer;

    private WebSite(Configuration cfg) {
        this.cfg = cfg;
        this.deployer = new Deployer(cfg.deployPath);
        this.docMeta = new DocMeta();
        this.registeredExtraJavaScripts = cfg.registeredExtraJavaScripts;
        this.componentsRegistry = new WebSiteComponentsRegistry();
        this.reactJsNashornEngine = new ReactJsNashornEngine();
        this.lunrIndexer = new LunrIndexer(reactJsNashornEngine);
        this.includeResourcesResolver = new RelativeToFileAndRootResourceResolver(cfg.tocPath.getParent());

        docMeta.setTitle(cfg.title);
        docMeta.setType(cfg.type);
        docMeta.setLogo(WebResource.withRelativePath(cfg.logoRelativePath));

        componentsRegistry.setIncludeResourcesResolver(includeResourcesResolver);

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
        generatePages();
        generateSearchIndex();
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
        markupParser = new MarkdownParser(componentsRegistry);
        pageByTocItem = new LinkedHashMap<>();
        allPagesProps = new ArrayList<>();
        extraJavaScripts = new ArrayList<>(registeredExtraJavaScripts);

        componentsRegistry.setParser(markupParser);
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

            includeResourcesResolver.setCurrentFilePath(markupPath);
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

    private void generatePages() {
        forEachPage(this::generatePage);
        buildJsonOfAllPages();
    }

    private void generateSearchIndex() {
        String jsonIndex = lunrIndexer.createJsonIndex(allPagesProps);
        deployer.deploy("search-index.json", jsonIndex);
    }

    private void buildJsonOfAllPages() {
        List<Map<String, ?>> listOfMaps = this.allPagesProps.stream().map(PageProps::toMap).collect(toList());
        String json = JsonUtils.serialize(listOfMaps);

        deployer.deploy("all-pages.json", json);
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
            Path pagePath = Paths.get(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension()).resolve("index.html");
            deployer.deploy(pagePath, html);
        } catch (Exception e) {
            throw new RuntimeException("Error during rendering of " + tocItem.getFileNameWithoutExtension(), e);
        }
    }

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
