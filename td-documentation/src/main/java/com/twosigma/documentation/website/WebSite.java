package com.twosigma.documentation.website;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.AuxiliaryFilesRegistry;
import com.twosigma.documentation.core.ResourcesResolverChain;
import com.twosigma.documentation.extensions.HttpBasedResourceResolver;
import com.twosigma.documentation.extensions.MultipleLocalLocationsResourceResolver;
import com.twosigma.documentation.html.*;
import com.twosigma.documentation.html.reactjs.ReactJsBundle;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.search.*;
import com.twosigma.documentation.structure.*;
import com.twosigma.documentation.web.WebResource;
import com.twosigma.documentation.web.extensions.WebSiteResourcesProviders;
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
import java.util.stream.Stream;

import static com.twosigma.documentation.parser.MarkupTypes.MARKDOWN;
import static com.twosigma.documentation.parser.MarkupTypes.SPHINX;
import static com.twosigma.documentation.website.ProgressReporter.reportPhase;
import static com.twosigma.utils.FileUtils.fileTextContent;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class WebSite {
    private static final String SEARCH_INDEX_FILE_NAME = "search-index.js";

    private PageToHtmlPageConverter pageToHtmlPageConverter;
    private MarkupParser markupParser;
    private final Deployer deployer;
    private final DocMeta docMeta;
    private final Configuration cfg;

    private Map<TocItem, Page> pageByTocItem;
    private GlobalSearchEntries globalSearchEntries;
    private LocalSearchEntries localSearchEntries;

    private TableOfContents toc;
    private WebSiteDocStructure docStructure;
    private Footer footer;
    private Map<TocItem, DocPageReactProps> pagePropsByTocItem;
    private List<WebResource> registeredExtraJavaScripts;
    private List<WebResource> extraJavaScriptsInFront;
    private List<WebResource> extraJavaScriptsInBack;

    private final WebSiteComponentsRegistry componentsRegistry;
    private final AuxiliaryFilesRegistry auxiliaryFilesRegistry;
    private final ReactJsBundle reactJsBundle;
    private final WebResource tocJavaScript;
    private final WebResource searchIndexJavaScript;

    private final MultipleLocalLocationsResourceResolver localResourceResolver;
    private final ResourcesResolverChain resourceResolver;

    private final MarkupParsingConfiguration markupParsingConfiguration;

    private WebSite(Configuration cfg) {
        this.cfg = cfg;
        this.deployer = new Deployer(cfg.deployPath);
        this.docMeta = cfg.docMeta;
        this.registeredExtraJavaScripts = cfg.registeredExtraJavaScripts;
        this.componentsRegistry = new WebSiteComponentsRegistry();
        this.resourceResolver = new ResourcesResolverChain();
        this.reactJsBundle = cfg.reactJsBundle;
        this.tocJavaScript = WebResource.withPath("toc.js");
        this.searchIndexJavaScript = WebResource.withPath(SEARCH_INDEX_FILE_NAME);
        this.auxiliaryFilesRegistry = new AuxiliaryFilesRegistry();
        this.markupParsingConfiguration = createMarkupParsingConfiguration();
        this.globalSearchEntries = new GlobalSearchEntries();
        this.localSearchEntries = new LocalSearchEntries();

        docMeta.setId(cfg.id);
        if (cfg.isPreviewEnabled) {
            docMeta.setPreviewEnabled(true);
        }

        componentsRegistry.setResourcesResolver(resourceResolver);

        localResourceResolver = new MultipleLocalLocationsResourceResolver(cfg.docRootPath);
        resourceResolver.addResolver(localResourceResolver);
        resourceResolver.addResolver(new HttpBasedResourceResolver());
        resourceResolver.initialize(findLookupLocations(cfg));

        WebSiteResourcesProviders.add(new WebSiteLogoExtension(cfg.docRootPath));
        WebSiteResourcesProviders.add(initFileBasedWebSiteExtension(cfg));

        reset();
    }

    public static Configuration withToc(Path path) {
        Configuration configuration = new Configuration();
        configuration.withTocPath(path);

        return configuration;
    }

    public void regenerate() {
        reset();
        generate();
    }

    public AuxiliaryFilesRegistry getAuxiliaryFilesRegistry() {
        return auxiliaryFilesRegistry;
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

    public ReactJsBundle getReactJsBundle() {
        return reactJsBundle;
    }

    public void generate() {
        reportPhase("building documentation");
        createTopLevelToc();
        parseMarkups();
        parseFooter();
        updateTocWithPageSections();
        validateCollectedLinks();
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
        docStructure.removeGlobalAnchorsForPath(markupPath(tocItem));

        parseMarkupAndUpdateTocItemAndSearch(tocItem);
        Page page = pageByTocItem.get(tocItem);

        tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());
        deployToc();

        HtmlPageAndPageProps pageProps = generatePage(tocItem, page);
        buildJsonOfAllPages();

        return pageProps;
    }

    public Set<TocItem> dependentTocItems(Path auxiliaryFile) {
        return auxiliaryFilesRegistry.tocItemsForPath(auxiliaryFile);
    }

    public TableOfContents getToc() {
        return toc;
    }

    public TableOfContents updateToc() {
        createTopLevelToc();
        forEachTocItemWithoutPage(this::parseMarkupAndUpdateTocItemAndSearch);
        updateTocWithPageSections();
        deployToc();
        return toc;
    }

    public void redeployAuxiliaryFileIfRequired(Path path) {
        if (auxiliaryFilesRegistry.requiresDeployment(path)) {
            deployAuxiliaryFile(auxiliaryFilesRegistry.auxiliaryFileByPath(path));
        }
    }

    private MarkupParsingConfiguration createMarkupParsingConfiguration() {
        switch (cfg.markupType) {
            case SPHINX:
                return new SphinxParsingConfiguration();
            default:
                return new MarkdownParsingConfiguration();
        }
    }

    private WebSiteUserExtensions initFileBasedWebSiteExtension(Configuration cfg) {
        if (cfg.extensionsDefPath == null || ! Files.exists(cfg.extensionsDefPath)) {
            return new WebSiteUserExtensions(resourceResolver, Collections.emptyMap());
        }

        String json = FileUtils.fileTextContent(cfg.extensionsDefPath);
        return new WebSiteUserExtensions(resourceResolver, JsonUtils.deserializeAsMap(json));
    }

    private void reset() {
        pageToHtmlPageConverter = new PageToHtmlPageConverter(docMeta, reactJsBundle);
        markupParser = markupParsingConfiguration.createMarkupParser(componentsRegistry);
        pageByTocItem = new LinkedHashMap<>();
        pagePropsByTocItem = new HashMap<>();
        extraJavaScriptsInFront = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScriptsInFront.add(tocJavaScript);
        extraJavaScriptsInBack = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScriptsInBack.add(searchIndexJavaScript);

        componentsRegistry.setDefaultParser(markupParser);
        componentsRegistry.setMarkdownParser(new MarkdownParser(componentsRegistry));
    }

    private void deployResources() {
        reportPhase("deploying resources");

        reactJsBundle.deploy(deployer);
        WebSiteResourcesProviders.cssResources().forEach(deployer::deploy);
        WebSiteResourcesProviders.jsResources().forEach(deployer::deploy);
        WebSiteResourcesProviders.jsClientOnlyResources().forEach(deployer::deploy);
        WebSiteResourcesProviders.additionalFilesToDeploy().forEach(deployer::deploy);
        cfg.webResources.forEach(deployer::deploy);
    }

    private void createTopLevelToc() {
        reportPhase("creating table of contents");
        toc = markupParsingConfiguration.createToc(cfg.tocPath);
        docStructure = new WebSiteDocStructure(docMeta, toc);
        componentsRegistry.setDocStructure(docStructure);
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
        toc.getTocItems().forEach(this::parseMarkupAndUpdateTocItemAndSearch);
    }

    private void parseFooter() {
        Path markupPath = cfg.footerPath;

        if (! Files.exists(markupPath)) {
            return;
        }

        reportPhase("parsing footer");

        localResourceResolver.setCurrentFilePath(markupPath);

        MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
        footer = new Footer(parserResult.getDocElement());
    }

    private void parseMarkupAndUpdateTocItemAndSearch(TocItem tocItem) {
        try {
            Path markupPath = markupPath(tocItem);

            localResourceResolver.setCurrentFilePath(markupPath);

            MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
            updateFilesAssociation(tocItem, parserResult.getAuxiliaryFiles());

            FileTime lastModifiedTime = Files.getLastModifiedTime(markupPath);
            Page page = new Page(parserResult.getDocElement(), lastModifiedTime, parserResult.getPageMeta());
            pageByTocItem.put(tocItem, page);

            tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());
            tocItem.setPageMeta(parserResult.getPageMeta());

            if (parserResult.getPageMeta().hasValue("title")) {
                tocItem.setPageTitle(parserResult.getPageMeta().getSingleValue("title"));
            }

            updateSearchEntries(tocItem, parserResult);
        } catch(Exception e) {
            throw new RuntimeException("error during parsing of <" + tocItem +
                    ">:" + e.getMessage(), e);
        }
    }

    private void updateSearchEntries(TocItem tocItem, MarkupParserResult parserResult) {
        List<GlobalSearchEntry> siteSearchEntries = parserResult.getSearchEntries().stream()
                .map(pageSearchEntry ->
                        new GlobalSearchEntry(
                                searchEntryUrl(tocItem, pageSearchEntry),
                                searchEntryTitle(tocItem, pageSearchEntry), pageSearchEntry.getText()))
                .collect(toList());

        globalSearchEntries.addAll(siteSearchEntries);
        localSearchEntries.add(new PageSearchEntries(tocItem, parserResult.getSearchEntries()));
    }

    private String searchEntryUrl(TocItem tocItem, PageSearchEntry pageSearchEntry) {
        DocUrl docUrl = tocItem.isIndex() ?
                DocUrl.indexUrl():
                new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), pageSearchEntry.getPageSectionId());
        return docStructure.createUrl(docUrl);
    }

    private String searchEntryTitle(TocItem tocItem, PageSearchEntry pageSearchEntry) {
        if (tocItem.isIndex()) {
            return docMeta.getTitle() + " " + (pageSearchEntry.getPageSectionTitle().isEmpty() ?
                    docMeta.getType() : pageSearchEntry.getPageSectionTitle());
        }

        return docMeta.getTitle() + ": " + tocItem.getPageTitle() + ", " + pageSearchEntry.getPageSectionTitle() +
                " [" + tocItem.getSectionTitle() + "]";
    }

    // each markup file may refer other files like code snippets or diagrams
    // we maintain dependency between them so we know which one triggers what page refresh during preview mode
    //
    private void updateFilesAssociation(TocItem tocItem, List<AuxiliaryFile> newAuxiliaryFiles) {
        newAuxiliaryFiles.forEach((af) -> auxiliaryFilesRegistry.updateFileAssociations(tocItem, af));
    }

    private Path markupPath(TocItem tocItem) {
        return markupParsingConfiguration.fullPath(cfg.docRootPath, tocItem);
    }

    private void generatePages() {
        reportPhase("generating the rest of HTML pages");
        forEachPage(this::generatePage);
        buildJsonOfAllPages();
    }

    private void generateSearchIndex() {
        reportPhase("generating search index");

        String jsIndexScript = localSearchEntries.buildIndexScript();
        deployer.deploy("search-index.js", jsIndexScript);

        String xmlExternalIndex = globalSearchEntries.toXml();
        deployer.deploy("search-entries.xml", xmlExternalIndex);
    }

    private void buildJsonOfAllPages() {
        List<Map<String, ?>> listOfMaps = this.pagePropsByTocItem
                .values().stream().map(DocPageReactProps::toMap).collect(toList());
        String json = JsonUtils.serialize(listOfMaps);

        deployer.deploy("all-pages.json", json);
    }

    private HtmlPageAndPageProps generatePage(TocItem tocItem, Page page) {
        try {
            HtmlPageAndPageProps htmlAndProps = pageToHtmlPageConverter.convert(
                    tocItem, page, createServerSideRenderer(tocItem), footer);

            pagePropsByTocItem.put(tocItem, htmlAndProps.getProps());
            extraJavaScriptsInFront.forEach(htmlAndProps.getHtmlPage()::addJavaScriptInFront);
            extraJavaScriptsInBack.forEach(htmlAndProps.getHtmlPage()::addJavaScript);

            String html = htmlAndProps.getHtmlPage().render(docMeta.getId());

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

    private RenderSupplier createServerSideRenderer(TocItem tocItem) {
        PageSearchEntries pageSearchEntries = localSearchEntries.searchEntriesByTocItem(tocItem);

        if (tocItem.isIndex()) {
            return () -> ServerSideSimplifiedRenderer.renderPageTextContent(pageSearchEntries) +
                    ServerSideSimplifiedRenderer.renderToc(toc);
        }

        return () -> ServerSideSimplifiedRenderer.renderPageTextContent(pageSearchEntries);
    }

    private void deployAuxiliaryFiles() {
        reportPhase("deploying auxiliary files (e.g. images)");
        auxiliaryFilesRegistry.getAuxiliaryFilesForDeployment().forEach(this::deployAuxiliaryFile);
    }

    private void deployAuxiliaryFile(AuxiliaryFile auxiliaryFile) {
        try {
            deployer.deploy(auxiliaryFile.getDeployRelativePath(), Files.readAllBytes(auxiliaryFile.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void forEachPage(PageConsumer consumer) {
        toc.getTocItems().forEach(tocItem -> {
            Page page = pageByTocItem.get(tocItem);
            consumer.consume(tocItem, page);
        });
    }

    private void forEachTocItemWithoutPage(TocItemConsumer consumer) {
        toc.getTocItems().forEach(tocItem -> {
            boolean isPagePresent = pageByTocItem.containsKey(tocItem);
            if (! isPagePresent) {
                consumer.consume(tocItem);
            }
        });
    }

    private void validateCollectedLinks() {
        reportPhase("validating links");
        docStructure.validateCollectedLinks();
    }

    private Stream<String> findLookupLocations(Configuration cfg) {
        Stream<String> root = Stream.of(cfg.docRootPath.toString());

        if (cfg.fileWithLookupPaths == null) {
            return root;
        }

        return Stream.concat(root, readLocationsFromFile(cfg.fileWithLookupPaths));
    }

    private Stream<String> readLocationsFromFile(String filesLookupFilePath) {
        Path lookupFilePath = cfg.docRootPath.resolve(filesLookupFilePath);
        if (! Files.exists(lookupFilePath)) {
            return Stream.empty();
        }

        String fileContent = FileUtils.fileTextContent(lookupFilePath);
        return Arrays.stream(fileContent.split("[;\n]"))
                .map(String::trim)
                .filter(e -> !e.isEmpty());
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
        private ReactJsBundle reactJsBundle;

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

        public Configuration withReactJsBundle(ReactJsBundle reactJsBundle) {
            this.reactJsBundle = reactJsBundle;
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
            String json = fileTextContent(path);
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
            WebSite webSite = new WebSite(this);
            webSite.generate();

            return webSite;
        }
    }
}
