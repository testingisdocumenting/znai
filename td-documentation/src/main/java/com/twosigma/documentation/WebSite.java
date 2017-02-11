package com.twosigma.documentation;

import com.google.gson.Gson;
import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludePlugins;
import com.twosigma.documentation.extensions.include.RelativeToFileAndRootResourceResolver;
import com.twosigma.documentation.html.*;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.parser.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.Page;
import com.twosigma.documentation.search.LunrIndexer;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;
import com.twosigma.utils.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
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
    private Map<Path, Set<TocItem>> tocItemsByAuxiliaryFilePath;
    private Set<AuxiliaryFile> auxiliaryFiles;

    private TableOfContents toc;
    private List<PageProps> allPagesProps;
    private List<WebResource> registeredExtraJavaScripts;
    private List<WebResource> extraJavaScripts;

    private final WebSiteComponentsRegistry componentsRegistry;
    private final RelativeToFileAndRootResourceResolver includeResourcesResolver;
    private final ReactJsNashornEngine reactJsNashornEngine;
    private final LunrIndexer lunrIndexer;
    private final WebResource tocJavaScript;

    private WebSite(Configuration cfg) {
        this.cfg = cfg;
        this.deployer = new Deployer(cfg.deployPath);
        this.docMeta = new DocMeta();
        this.registeredExtraJavaScripts = cfg.registeredExtraJavaScripts;
        this.componentsRegistry = new WebSiteComponentsRegistry();
        this.reactJsNashornEngine = initJsEngine();
        this.lunrIndexer = new LunrIndexer(reactJsNashornEngine);
        this.tocJavaScript = WebResource.withRelativePath("toc.js");
        this.includeResourcesResolver = new RelativeToFileAndRootResourceResolver(cfg.tocPath.getParent());
        this.tocItemsByAuxiliaryFilePath = new HashMap<>();
        this.auxiliaryFiles = new HashSet<>();

        docMeta.setTitle(cfg.title);
        docMeta.setType(cfg.type);
        if (cfg.isPreviewEnabled) {
            docMeta.setPreviewEnabled(true);
        }
        docMeta.setLogo(WebResource.withRelativePath(cfg.logoRelativePath));

        componentsRegistry.setIncludeResourcesResolver(includeResourcesResolver);

        reset();
    }

    private ReactJsNashornEngine initJsEngine() {
        reportPhase("initializing ReactJS server side engine");
        ReactJsNashornEngine engine = new ReactJsNashornEngine();
        engine.getNashornEngine().eval("toc = []");
        engine.loadLibraries();
        return engine;
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
        reportPhase("building documentation");
        createTopLevelToc();
        parseMarkups();
        updateTocWithPageSections();
        generatePages();
        generateSearchIndex();
        deployToc();
        deployAuxiliaryFiles();
        deployResources();
    }

    public TocItem tocItemByPath(Path path) {
        return toc.getTocItems().stream().filter(ti ->
                path.toAbsolutePath().getParent().getFileName().toString().equals(ti.getDirName()) &&
                        path.getFileName().toString().equals(ti.getFileNameWithoutExtension() + ".md")).findFirst().orElse(null);
    }

    public HtmlPageAndPageProps regeneratePage(TocItem tocItem) {
        parseMarkup(tocItem);
        final Page page = pageByTocItem.get(tocItem);
        return generatePage(tocItem, page);
    }

    public Set<TocItem> dependentTocItems(Path auxiliaryFile) {
        Set<TocItem> paths = tocItemsByAuxiliaryFilePath.get(auxiliaryFile);
        return (paths == null) ? Collections.emptySet() : paths;
    }

    public TableOfContents updateToc() {
        createTopLevelToc();
        updateTocWithPageSections();
        deployToc();
        return toc;
    }

    private void reset() {
        pageToHtmlPageConverter = new PageToHtmlPageConverter(docMeta, toc, reactJsNashornEngine);
        markupParser = new MarkdownParser(componentsRegistry);
        pageByTocItem = new LinkedHashMap<>();
        allPagesProps = new ArrayList<>();
        extraJavaScripts = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScripts.add(tocJavaScript);

        componentsRegistry.setParser(markupParser);
    }

    private void deployResources() {
        reportPhase("deploying resources");
        reactJsNashornEngine.getReactJsBundle().deploy(deployer);
        cfg.webResources.forEach(deployer::deploy);
    }

    private void createTopLevelToc() {
        reportPhase("creating table of contents");
        toc = TableOfContents.fromNestedText(fileTextContent(cfg.tocPath));
        toc.addTocItem("", "index");
    }

    /**
     * Table of Contents has a page placement information available in the external resource.
     * Additional page structure information comes after parsing file. Hence phased approach.
     */
    private void updateTocWithPageSections() {
        forEachPage(((tocItem, page) -> {
            tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());
        }));
    }

    private void deployToc() {
        reportPhase("deploying table of contents");
        String tocJson = JsonUtils.serializePrettyPrint(toc.toListOfMaps());

        deployer.deploy(tocJavaScript, "toc = " + tocJson);

        reactJsNashornEngine.getNashornEngine().bind("tocJson", tocJson);
        reactJsNashornEngine.getNashornEngine().eval("setTocJson(tocJson)");
    }

    private void parseMarkups() {
        reportPhase("parsing markup files");
        toc.getTocItems().forEach(this::parseMarkup);
    }

    private void parseMarkup(final TocItem tocItem) {
        try {
            Path markupPath = markupPath(tocItem);

            includeResourcesResolver.setCurrentFilePath(markupPath);
            resetPlugins(markupPath);

            MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
            updateFilesAssociation(tocItem, parserResult.getAuxiliaryFiles());

            final Page page = new Page(parserResult.getDocElement());
            pageByTocItem.put(tocItem, page);
        } catch(Exception e) {
            throw new RuntimeException("error during parsing of " + tocItem.getFileNameWithoutExtension() +
                    ":" + e.getMessage(), e);
        }
    }

    // each markup file may refer other files like code snippets or diagrams
    // we maintain dependency between them so we know which one triggers what page refresh during preview mode
    //
    private void updateFilesAssociation(TocItem tocItem, List<AuxiliaryFile> newAuxiliaryFiles) {
        newAuxiliaryFiles.forEach((af) -> {
            Set<TocItem> tocItems = tocItemsByAuxiliaryFilePath.computeIfAbsent(af.getPath(), k -> new HashSet<>());
            tocItems.add(tocItem);

            auxiliaryFiles.add(af);
        });
    }

    private Path markupPath(final TocItem tocItem) {
        return cfg.tocPath.toAbsolutePath().getParent().resolve(
                tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension() + ".md"); // TODO extension auto figure out
    }

    private void resetPlugins(final Path markdownPath) {
        final IncludeContext context = new IncludeContext(markdownPath);
        IncludePlugins.reset(context);
    }

    private void generatePages() {
        reportPhase("generating the rest of HTML pages");
        forEachPage(this::generatePage);
        buildJsonOfAllPages();
    }

    private void generateSearchIndex() {
        reportPhase("generating search index");
        String jsonIndex = lunrIndexer.createJsonIndex(allPagesProps);
        deployer.deploy("search-index.json", jsonIndex);
    }

    private void buildJsonOfAllPages() {
        List<Map<String, ?>> listOfMaps = this.allPagesProps.stream().map(PageProps::toMap).collect(toList());
        String json = JsonUtils.serialize(listOfMaps);

        deployer.deploy("all-pages.json", json);
    }

    private HtmlPageAndPageProps generatePage(final TocItem tocItem, final Page page) {
        try {
            resetPlugins(markupPath(tocItem)); // TODO reset at render phase only?

            boolean isIndex = isIndexToc(tocItem);

            // we are inside directory, so nest level is 1 for extra resources
            HtmlRenderContext renderContext = HtmlRenderContext.nested(isIndex ? 0 : 1);

            final HtmlPageAndPageProps htmlAndProps = pageToHtmlPageConverter.convert(toc, tocItem, page, renderContext);

            allPagesProps.add(htmlAndProps.getProps());
            extraJavaScripts.forEach(htmlAndProps.getHtmlPage()::addJavaScriptInFront);

            final String html = htmlAndProps.getHtmlPage().render(renderContext);

            Path pagePath = isIndex ? Paths.get("index.html") :
                    Paths.get(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension()).resolve("index.html");

            deployer.deploy(pagePath, html);

            return htmlAndProps;
        } catch (Exception e) {
            throw new RuntimeException("error during rendering of " + tocItem.getFileNameWithoutExtension() + ": " + e.getMessage(),
                    e);
        }
    }

    private boolean isIndexToc(TocItem tocItem) {
        return tocItem.getDirName().equals("") && tocItem.getFileNameWithoutExtension().equals("index");
    }

    private void deployAuxiliaryFiles() {
        reportPhase("deploying auxiliary files (e.g. images)");
        auxiliaryFiles.stream().filter(AuxiliaryFile::isRequiresDeployment)
                .forEach(this::deployAuxiliaryFile);
    }

    private void deployAuxiliaryFile(AuxiliaryFile auxiliaryFile) {
        Path origin = auxiliaryFile.getPath().toAbsolutePath();
        Path relative = cfg.tocPath.getParent().relativize(origin);
        try {
            deployer.deploy(relative, Files.readAllBytes(origin));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void forEachPage(PageConsumer consumer) {
        toc.getTocItems().forEach(tocItem -> {
            final Page page = pageByTocItem.get(tocItem);
            consumer.consume(tocItem, page);
        });
    }

    private void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
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
        public List<WebResource> registeredExtraJavaScripts;
        private boolean isPreviewEnabled;

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

        public Configuration withEnabledPreview(boolean isPreviewEnabled) {
            this.isPreviewEnabled = isPreviewEnabled;
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
