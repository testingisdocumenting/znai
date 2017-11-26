package com.twosigma.documentation.website;

import com.google.gson.Gson;
import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.codesnippets.JsBasedCodeSnippetsTokenizer;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.Plugins;
import com.twosigma.documentation.extensions.MultipleLocationsResourceResolver;
import com.twosigma.documentation.html.*;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.parser.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.sphinx.SphinxDocTreeParser;
import com.twosigma.documentation.structure.Page;
import com.twosigma.documentation.search.LunrIndexer;
import com.twosigma.documentation.structure.*;
import com.twosigma.documentation.website.markups.MarkdownParsingConfiguration;
import com.twosigma.documentation.website.markups.MarkupParsingConfiguration;
import com.twosigma.documentation.website.markups.SphinxParsingConfiguration;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.twosigma.documentation.parser.MarkupTypes.MARKDOWN;
import static com.twosigma.documentation.parser.MarkupTypes.SPHINX;
import static com.twosigma.utils.FileUtils.fileTextContent;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class WebSite implements DocStructure {
    private PageToHtmlPageConverter pageToHtmlPageConverter;
    private MarkupParser markupParser;
    private final Deployer deployer;
    private final DocMeta docMeta;
    private Configuration cfg;

    private Map<TocItem, Page> pageByTocItem;
    private Map<Path, Set<TocItem>> tocItemsByAuxiliaryFilePath;
    private Map<Path, AuxiliaryFile> auxiliaryFiles;

    private TableOfContents toc;
    private Footer footer;
    private List<PageProps> allPagesProps;
    private List<WebResource> registeredExtraJavaScripts;
    private List<WebResource> extraJavaScripts;

    private List<LinkToValidate> linksToValidate;

    private final WebSiteComponentsRegistry componentsRegistry;
    private final MultipleLocationsResourceResolver resourceResolver;
    private final ReactJsNashornEngine reactJsNashornEngine;
    private final LunrIndexer lunrIndexer;
    private final CodeTokenizer codeTokenizer;
    private final WebResource tocJavaScript;
    private final WebSiteExtensions webSiteExtensions;

    private final MarkupParsingConfiguration markupParsingConfiguration;

    private AuxiliaryFileListener auxiliaryFileListener;

    private WebSite(Configuration cfg) {
        this.cfg = cfg;
        this.linksToValidate = new ArrayList<>();
        this.deployer = new Deployer(cfg.deployPath);
        this.docMeta = cfg.docMeta;
        this.registeredExtraJavaScripts = cfg.registeredExtraJavaScripts;
        this.componentsRegistry = new WebSiteComponentsRegistry();
        this.reactJsNashornEngine = initJsEngine();
        this.lunrIndexer = new LunrIndexer(reactJsNashornEngine);
        this.codeTokenizer = new JsBasedCodeSnippetsTokenizer(reactJsNashornEngine.getNashornEngine());
        this.tocJavaScript = WebResource.withPath("toc.js");
        this.resourceResolver = new MultipleLocationsResourceResolver(cfg.docRootPath, findLookupLocations(cfg));
        this.webSiteExtensions = initWebSiteExtensions(cfg);
        this.tocItemsByAuxiliaryFilePath = new HashMap<>();
        this.markupParsingConfiguration = createMarkupParsingConfiguration();
        this.auxiliaryFiles = new HashMap<>();

        docMeta.setId(cfg.id);
        if (cfg.isPreviewEnabled) {
            docMeta.setPreviewEnabled(true);
        }
        docMeta.setLogo(WebResource.withPath(cfg.logoRelativePath));

        componentsRegistry.setResourcesResolver(resourceResolver);
        componentsRegistry.setCodeTokenizer(codeTokenizer);
        componentsRegistry.setDocStructure(this);

        reset();
    }

    public static Configuration withToc(Path path) {
        final Configuration configuration = new Configuration();
        configuration.withTocPath(path);

        return configuration;
    }

    public void regenerate() {
        reset();
        generate();
    }

    public void setAuxiliaryFileListener(AuxiliaryFileListener auxiliaryFileListener) {
        this.auxiliaryFileListener = auxiliaryFileListener;
    }

    public Collection<AuxiliaryFile> getAuxiliaryFiles() {
        return auxiliaryFiles.values();
    }

    public Path getDeployRoot() {
        return cfg.deployPath;
    }

    public Path getTocPath() {
        return cfg.tocPath;
    }

    public DocMeta getDocMeta() {
        return docMeta;
    }

    public void generate() {
        reportPhase("building documentation");
        createTopLevelToc();
        parseMarkups();
        parseFooter();
        updateTocWithPageSections();
        validateCollectedLinks();
        setGlobalToc();
        generatePages();
        generateSearchIndex();
        deployToc();
        deployAuxiliaryFiles();
        deployResources();
    }

    public TocItem tocItemByPath(Path path) {
        if (path.getFileName().toString().startsWith(TocItem.INDEX + ".")) {
            return toc.getIndex();
        }

        return toc.getTocItems().stream().filter(ti ->
                path.toAbsolutePath().getParent().getFileName().toString().equals(ti.getDirName()) &&
                        path.getFileName().toString().equals(
                                ti.getFileNameWithoutExtension() + "." + markupParsingConfiguration.filesExtension()))
                .findFirst().orElse(null);
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

    public TableOfContents getToc() {
        return toc;
    }

    public TableOfContents updateToc() {
        createTopLevelToc();
        forEachTocItemWithoutPage(this::parseMarkup);
        updateTocWithPageSections();
        deployToc();
        return toc;
    }

    @Override
    public void validateUrl(Path path, String sectionWithLinkTitle, DocUrl docUrl) {
        if (docUrl.isGlobalUrl() || docUrl.isIndexUrl()) {
            return;
        }

        linksToValidate.add(new LinkToValidate(path, sectionWithLinkTitle, docUrl));
    }

    @Override
    public String createUrl(DocUrl docUrl) {
        if (docUrl.isGlobalUrl()) {
            return docUrl.getUrl();
        }

        if (docUrl.isIndexUrl()) {
            return "/" + docMeta.getId();
        }

        String base = "/" + docMeta.getId() + "/" + docUrl.getDirName() + "/" + docUrl.getFileName();
        return base + (docUrl.getPageSectionId().isEmpty() ? "" : "#" + docUrl.getPageSectionId());
    }

    @Override
    public String prefixUrlWithProductId(String url) {
        url = url.toLowerCase();
        if (url.startsWith("http")) {
            return url;
        }

        return "/" + docMeta.getId() + "/" + url;
    }

    public void redeployAuxiliaryFileIfRequired(Path path) {
        AuxiliaryFile auxiliaryFile = auxiliaryFiles.get(path);
        if (auxiliaryFile == null) {
            return;
        }

        if (! auxiliaryFile.isDeploymentRequired()) {
            return;
        }

        deployAuxiliaryFile(auxiliaryFile);
    }

    private MarkupParsingConfiguration createMarkupParsingConfiguration() {
        switch (cfg.markupType) {
            case SPHINX:
                return new SphinxParsingConfiguration();
            default:
                return new MarkdownParsingConfiguration();
        }
    }

    private WebSiteExtensions initWebSiteExtensions(Configuration cfg) {
        if (cfg.extensionsDefPath == null || ! Files.exists(cfg.extensionsDefPath)) {
            return new WebSiteExtensions(resourceResolver, Collections.emptyMap());
        }

        String json = FileUtils.fileTextContent(cfg.extensionsDefPath);
        return new WebSiteExtensions(resourceResolver, JsonUtils.deserializeAsMap(json));
    }

    private ReactJsNashornEngine initJsEngine() {
        reportPhase("initializing ReactJS server side engine");
        ReactJsNashornEngine engine = new ReactJsNashornEngine();
        engine.getNashornEngine().eval("toc = []");
        engine.loadLibraries();
        return engine;
    }

    private void reset() {
        pageToHtmlPageConverter = new PageToHtmlPageConverter(docMeta, webSiteExtensions, reactJsNashornEngine);
        markupParser = markupParsingConfiguration.createMarkupParser(componentsRegistry);
        pageByTocItem = new LinkedHashMap<>();
        allPagesProps = new ArrayList<>();
        extraJavaScripts = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScripts.add(tocJavaScript);

        componentsRegistry.setParser(markupParser);
    }

    private void deployResources() {
        reportPhase("deploying resources");
        reactJsNashornEngine.getReactJsBundle().deploy(deployer);
        webSiteExtensions.getCssResources().forEach(deployer::deploy);
        cfg.webResources.forEach(deployer::deploy);
    }

    private void createTopLevelToc() {
        reportPhase("creating table of contents");
        toc = markupParsingConfiguration.createToc(cfg.tocPath);
    }

    /**
     * Table of Contents has a page placement information available in the external resource.
     * Additional page structure information comes after parsing file. Hence phased approach.
     */
    private void updateTocWithPageSections() {
        forEachPage(((tocItem, page) -> tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles())));
    }

    private void deployToc() {
        reportPhase("deploying table of contents");
        String tocJson = JsonUtils.serializePrettyPrint(toc.toListOfMaps());
        deployer.deploy(tocJavaScript, "toc = " + tocJson);
    }

    private void parseMarkups() {
        reportPhase("parsing markup files");
        toc.getTocItems().forEach(this::parseMarkup);
    }

    private void parseFooter() {
        final Path markupPath = cfg.footerPath;

        if (! Files.exists(markupPath)) {
            return;
        }

        reportPhase("parsing footer");

        resourceResolver.setCurrentFilePath(markupPath);
        resetPlugins(markupPath);

        MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
        footer = new Footer(parserResult.getDocElement());
    }

    private void parseMarkup(final TocItem tocItem) {
        try {
            Path markupPath = markupPath(tocItem);

            resourceResolver.setCurrentFilePath(markupPath);
            resetPlugins(markupPath);

            MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
            updateFilesAssociation(tocItem, parserResult.getAuxiliaryFiles());

            FileTime lastModifiedTime = Files.getLastModifiedTime(markupPath);
            final Page page = new Page(parserResult.getDocElement(), lastModifiedTime);
            pageByTocItem.put(tocItem, page);

            tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());

            List<String> title = parserResult.getProperties().get("title");
            if (title != null) {
                tocItem.setPageTitle(title.get(0));
            }
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

            auxiliaryFiles.put(af.getPath(), af);
            if (auxiliaryFileListener != null) {
                auxiliaryFileListener.onAuxiliaryFile(af);
            }
        });
    }

    private Path markupPath(final TocItem tocItem) {
        return markupParsingConfiguration.fullPath(cfg.docRootPath, tocItem);
    }

    private void resetPlugins(final Path markupPath) {
        final IncludeContext context = new IncludeContext(markupPath);
        Plugins.reset(context);
    }

    // we share TOC between pages as opposite to setting TOC to each page
    // global TOC is required to be set for server side page rendering
    private void setGlobalToc() {
        reportPhase("setting global TOC");

        String tocJson = JsonUtils.serializePrettyPrint(toc.toListOfMaps());
        reactJsNashornEngine.getNashornEngine().bind("tocJson", tocJson);
        reactJsNashornEngine.getNashornEngine().eval("setTocJson(tocJson)");
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

            final HtmlPageAndPageProps htmlAndProps = pageToHtmlPageConverter.convert(tocItem, page, footer);

            allPagesProps.add(htmlAndProps.getProps());
            extraJavaScripts.forEach(htmlAndProps.getHtmlPage()::addJavaScriptInFront);

            final String html = htmlAndProps.getHtmlPage().render(docMeta.getId());

            Path pagePath = tocItem.isIndex() ? Paths.get("index.html") :
                    Paths.get(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension()).resolve("index.html");

            deployer.deploy(pagePath, html);

            return htmlAndProps;
        } catch (Exception e) {
            throw new RuntimeException("error during rendering of " +
                    tocItem.getFileNameWithoutExtension() + ": " + e.getMessage(),
                    e);
        }
    }

    private void deployAuxiliaryFiles() {
        reportPhase("deploying auxiliary files (e.g. images)");
        auxiliaryFiles.values().stream().filter(AuxiliaryFile::isDeploymentRequired)
                .forEach(this::deployAuxiliaryFile);
    }

    private void deployAuxiliaryFile(AuxiliaryFile auxiliaryFile) {
        Path origin = auxiliaryFile.getPath().toAbsolutePath();
        Path relative = resourceResolver.docRootRelativePath(origin);
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

    private List<TocItem> forEachTocItemWithoutPage(TocItemConsumer consumer) {
        List<TocItem> withoutPages = new ArrayList<>();

        toc.getTocItems().forEach(tocItem -> {
            boolean isPagePresent = pageByTocItem.containsKey(tocItem);
            if (! isPagePresent) {
                consumer.consume(tocItem);
                withoutPages.add(tocItem);
            }
        });

        return withoutPages;
    }

    private void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
    }

    private void validateCollectedLinks() {
        reportPhase("validating links");
        String validationErrorMessage = linksToValidate.stream().map(this::validateLink)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(joining("\n\n"));

        if (!validationErrorMessage.isEmpty()) {
            throw new IllegalArgumentException(validationErrorMessage  + "\n ");
        }
    }

    private Optional<String> validateLink(LinkToValidate link) {
        String url = link.docUrl.getDirName() + "/" + link.docUrl.getFileName() +
                (link.docUrl.getPageSectionId().isEmpty() ?  "" : "#" + link.docUrl.getPageSectionId());

        Supplier<String> validationMessage = () -> "can't find a page associated with: " + url +
                "\ncheck file: " + link.path + ", section title: " + link.sectionWithLinkTitle;

        TocItem tocItem = toc.findTocItem(link.docUrl.getDirName(), link.docUrl.getFileName());
        if (tocItem == null) {
            return Optional.of(validationMessage.get());
        }

        if (!link.docUrl.getPageSectionId().isEmpty() && !tocItem.hasPageSection(link.docUrl.getPageSectionId())) {
            return Optional.of(validationMessage.get());
        }

        return Optional.empty();
    }

    private Stream<Path> findLookupLocations(Configuration cfg) {
        Stream<Path> root = Stream.of(cfg.docRootPath);

        if (cfg.fileWithLookupPaths == null) {
            return root;
        }

        return Stream.concat(root, readLocationsFromFile(cfg.fileWithLookupPaths));
    }

    private Stream<Path> readLocationsFromFile(String filesLookupFilePath) {
        Path lookupFilePath = cfg.docRootPath.resolve(filesLookupFilePath);
        if (! Files.exists(lookupFilePath)) {
            return Stream.empty();
        }

        String fileContent = FileUtils.fileTextContent(lookupFilePath);
        return Arrays.stream(fileContent.split("[;\n]"))
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .map(e -> Paths.get(e))
                .map(p -> p.isAbsolute() ? p : cfg.docRootPath.resolve(p));
    }

    private interface PageConsumer {
        void consume(TocItem tocItem, Page page);
    }

    private interface TocItemConsumer {
        void consume(TocItem tocItem);
    }

    public static class Configuration {
        private Path deployPath;
        private Path docRootPath;
        private Path tocPath;
        private Path footerPath;
        private Path extensionsDefPath;
        private List<WebResource> webResources;
        private String id;
        private String title;
        private String type;
        private String fileWithLookupPaths;
        private String logoRelativePath;
        private List<WebResource> registeredExtraJavaScripts;
        private boolean isPreviewEnabled;
        private String markupType = MARKDOWN;
        private DocMeta docMeta = new DocMeta(Collections.emptyMap());

        private Configuration() {
            webResources = new ArrayList<>();
            registeredExtraJavaScripts = new ArrayList<>();
        }

        public Configuration withMarkupType(String markupType) {
            this.markupType = markupType;
            return this;
        }

        public Configuration withTocPath(Path path) {
            tocPath = path.toAbsolutePath();
            docRootPath = tocPath.getParent();
            return this;
        }

        public Configuration withFooterPath(Path path) {
            footerPath = path.toAbsolutePath();
            return this;
        }

        public Configuration withExtensionsDefPath(Path path) {
            extensionsDefPath = path.toAbsolutePath();
            return this;
        }

        public Configuration withWebResources(WebResource... resources) {
            webResources.addAll(Arrays.asList(resources));
            return this;
        }

        public Configuration withExtraJavaScripts(WebResource... webResources) {
            this.registeredExtraJavaScripts.addAll(Arrays.asList(webResources));
            return this;
        }

        public Configuration withId(String id) {
            this.id = id;
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

        public Configuration withFileWithLookupPaths(String fileWithLookupPaths) {
            this.fileWithLookupPaths = fileWithLookupPaths;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Configuration withMetaFromJsonFile(Path path) {
            final String json = fileTextContent(path);
            docMeta = new DocMeta(json);

            withTitle(docMeta.getTitle());
            withType(docMeta.getType());

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

    private class LinkToValidate {
        private final Path path;
        private final String sectionWithLinkTitle;
        private final DocUrl docUrl;

        LinkToValidate(Path path, String sectionWithLinkTitle, DocUrl docUrl) {
            this.path = path;
            this.sectionWithLinkTitle = sectionWithLinkTitle;
            this.docUrl = docUrl;
        }
    }
}
