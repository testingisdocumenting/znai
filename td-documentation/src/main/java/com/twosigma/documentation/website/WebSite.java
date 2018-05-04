package com.twosigma.documentation.website;

import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.codesnippets.JsBasedCodeSnippetsTokenizer;
import com.twosigma.documentation.core.AuxiliaryFilesRegistry;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.extensions.MultipleLocationsResourceResolver;
import com.twosigma.documentation.html.*;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.search.LunrIndexer;
import com.twosigma.documentation.search.PageSearchEntry;
import com.twosigma.documentation.search.SiteSearchEntries;
import com.twosigma.documentation.search.SiteSearchEntry;
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
    private PageToHtmlPageConverter pageToHtmlPageConverter;
    private MarkupParser markupParser;
    private final Deployer deployer;
    private final DocMeta docMeta;
    private Configuration cfg;

    private Map<TocItem, Page> pageByTocItem;
    private SiteSearchEntries searchEntries;

    private TableOfContents toc;
    private WebSiteDocStructure docStructure;
    private Footer footer;
    private List<DocPageReactProps> allPagesProps;
    private List<WebResource> registeredExtraJavaScripts;
    private List<WebResource> extraJavaScripts;

    private final WebSiteComponentsRegistry componentsRegistry;
    private final AuxiliaryFilesRegistry auxiliaryFilesRegistry;
    private final MultipleLocationsResourceResolver resourceResolver;
    private final ReactJsNashornEngine reactJsNashornEngine;
    private final LunrIndexer lunrIndexer;
    private final CodeTokenizer codeTokenizer;
    private final WebResource tocJavaScript;
    private final WebSiteExtensions webSiteExtensions;

    private final MarkupParsingConfiguration markupParsingConfiguration;

    private WebSite(Configuration cfg) {
        this.cfg = cfg;
        this.deployer = new Deployer(cfg.deployPath);
        this.docMeta = cfg.docMeta;
        this.registeredExtraJavaScripts = cfg.registeredExtraJavaScripts;
        this.componentsRegistry = new WebSiteComponentsRegistry();
        this.resourceResolver = new MultipleLocationsResourceResolver(cfg.docRootPath, findLookupLocations(cfg));
        this.webSiteExtensions = initWebSiteExtensions(cfg);
        this.reactJsNashornEngine = cfg.reactJsNashornEngine;
        this.lunrIndexer = new LunrIndexer(reactJsNashornEngine);
        this.codeTokenizer = new JsBasedCodeSnippetsTokenizer(reactJsNashornEngine.getNashornEngine());
        this.tocJavaScript = WebResource.withPath("toc.js");
        this.auxiliaryFilesRegistry = new AuxiliaryFilesRegistry();
        this.markupParsingConfiguration = createMarkupParsingConfiguration();
        this.searchEntries = new SiteSearchEntries();

        docMeta.setId(cfg.id);
        if (cfg.isPreviewEnabled) {
            docMeta.setPreviewEnabled(true);
        }
        docMeta.setLogo(WebResource.withPath(cfg.logoRelativePath));

        componentsRegistry.setResourcesResolver(resourceResolver);
        componentsRegistry.setCodeTokenizer(codeTokenizer);

        loadCustomJsLibraries();

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

    public ReactJsNashornEngine getReactJsNashornEngine() {
        return reactJsNashornEngine;
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
        docStructure.removeGlobalAnchorsForPath(markupPath(tocItem));

        parseMarkupAndUpdateTocItem(tocItem);
        Page page = pageByTocItem.get(tocItem);

        return generatePage(tocItem, page);
    }

    public Set<TocItem> dependentTocItems(Path auxiliaryFile) {
        return auxiliaryFilesRegistry.tocItemsForPath(auxiliaryFile);
    }

    public TableOfContents getToc() {
        return toc;
    }

    public TableOfContents updateToc() {
        createTopLevelToc();
        forEachTocItemWithoutPage(this::parseMarkupAndUpdateTocItem);
        updateTocWithPageSections();
        deployToc();
        return toc;
    }

    public void redeployAuxiliaryFileIfRequired(Path path) {
        if (auxiliaryFilesRegistry.requiresDeployment(path)) {
            deployAuxiliaryFile(path);
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

    private WebSiteExtensions initWebSiteExtensions(Configuration cfg) {
        if (cfg.extensionsDefPath == null || ! Files.exists(cfg.extensionsDefPath)) {
            return new WebSiteExtensions(resourceResolver, Collections.emptyMap());
        }

        String json = FileUtils.fileTextContent(cfg.extensionsDefPath);
        return new WebSiteExtensions(resourceResolver, JsonUtils.deserializeAsMap(json));
    }

    private void loadCustomJsLibraries() {
        reportPhase("loading custom js libraries");
        reactJsNashornEngine.loadCustomLibraries(webSiteExtensions.getJsResources());
    }

    private void reset() {
        pageToHtmlPageConverter = new PageToHtmlPageConverter(docMeta, webSiteExtensions, reactJsNashornEngine);
        markupParser = markupParsingConfiguration.createMarkupParser(componentsRegistry);
        pageByTocItem = new LinkedHashMap<>();
        allPagesProps = new ArrayList<>();
        extraJavaScripts = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScripts.add(tocJavaScript);

        componentsRegistry.setDefaultParser(markupParser);
        componentsRegistry.setMarkdownParser(new MarkdownParser(componentsRegistry));
    }

    private void deployResources() {
        reportPhase("deploying resources");
        reactJsNashornEngine.getReactJsBundle().deploy(deployer);
        webSiteExtensions.getCssResources().forEach(deployer::deploy);
        webSiteExtensions.getJsResources().forEach(deployer::deploy);
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
        toc.getTocItems().forEach(this::parseMarkupAndUpdateTocItem);
    }

    private void parseFooter() {
        Path markupPath = cfg.footerPath;

        if (! Files.exists(markupPath)) {
            return;
        }

        reportPhase("parsing footer");

        resourceResolver.setCurrentFilePath(markupPath);

        MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
        footer = new Footer(parserResult.getDocElement());
    }

    private void parseMarkupAndUpdateTocItem(TocItem tocItem) {
        try {
            Path markupPath = markupPath(tocItem);

            resourceResolver.setCurrentFilePath(markupPath);

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
        List<SiteSearchEntry> siteSearchEntries = parserResult.getSearchEntries().stream()
                .map(pageSearchEntry ->
                        new SiteSearchEntry(
                                searchEntryUrl(tocItem, pageSearchEntry),
                                searchEntryTitle(tocItem, pageSearchEntry), pageSearchEntry.getText()))
                .collect(toList());

        searchEntries.addAll(siteSearchEntries);
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

        String xmlExternalIndex = searchEntries.toXml();
        deployer.deploy("search-entries.xml", xmlExternalIndex);
    }

    private void buildJsonOfAllPages() {
        List<Map<String, ?>> listOfMaps = this.allPagesProps.stream().map(DocPageReactProps::toMap).collect(toList());
        String json = JsonUtils.serialize(listOfMaps);

        deployer.deploy("all-pages.json", json);
    }

    private HtmlPageAndPageProps generatePage(TocItem tocItem, Page page) {
        try {
            HtmlPageAndPageProps htmlAndProps = pageToHtmlPageConverter.convert(tocItem, page, footer);

            allPagesProps.add(htmlAndProps.getProps());
            extraJavaScripts.forEach(htmlAndProps.getHtmlPage()::addJavaScriptInFront);

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

    private void deployAuxiliaryFiles() {
        reportPhase("deploying auxiliary files (e.g. images)");
        auxiliaryFilesRegistry.getAuxiliaryFilePathsRequiringDeployment().forEach(this::deployAuxiliaryFile);
    }

    private void deployAuxiliaryFile(Path auxiliaryFilePath) {
        Path relative = resourceResolver.docRootRelativePath(auxiliaryFilePath);
        try {
            deployer.deploy(relative, Files.readAllBytes(auxiliaryFilePath));
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
        private ReactJsNashornEngine reactJsNashornEngine;

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

        public Configuration withReactJsNashornEngine(ReactJsNashornEngine engine) {
            reactJsNashornEngine = engine;
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
