/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.website;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.AuxiliaryFilesRegistry;
import org.testingisdocumenting.znai.resources.*;
import org.testingisdocumenting.znai.html.*;
import org.testingisdocumenting.znai.html.reactjs.ReactJsBundle;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.reference.GlobalDocReferences;
import org.testingisdocumenting.znai.search.*;
import org.testingisdocumenting.znai.structure.*;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;
import org.testingisdocumenting.znai.web.WebResource;
import org.testingisdocumenting.znai.web.extensions.WebSiteResourcesProviders;
import org.testingisdocumenting.znai.parser.MarkupParsingConfiguration;
import org.testingisdocumenting.znai.parser.MarkupParsingConfigurations;
import org.testingisdocumenting.znai.website.modifiedtime.FileBasedPageModifiedTime;
import org.testingisdocumenting.znai.website.modifiedtime.PageModifiedTimeStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.parser.MarkupTypes.MARKDOWN;
import static org.testingisdocumenting.znai.utils.FileUtils.fileTextContent;
import static org.testingisdocumenting.znai.website.ProgressReporter.reportPhase;
import static java.util.stream.Collectors.toList;

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
    private final GlobalDocReferences globalDocReferences;
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
    private final WebResource globalAssetsJavaScript;
    private final WebResource globalDocReferencesJavaScript;
    private final WebResource searchIndexJavaScript;

    private final MultipleLocalLocationsResourceResolver localResourceResolver;
    private final ResourcesResolverChain resourceResolver;

    private final MarkupParsingConfiguration markupParsingConfiguration;

    private final Map<AuxiliaryFile, Long> auxiliaryFilesLastUpdateTime;

    private final PageModifiedTimeStrategy pageModifiedTimeStrategy;

    private WebSite(Configuration siteConfig) {
        cfg = siteConfig;
        deployer = new Deployer(siteConfig.deployPath);
        docMeta = siteConfig.docMeta;
        pageModifiedTimeStrategy = siteConfig.pageModifiedTimeStrategy != null ?
                siteConfig.pageModifiedTimeStrategy : new FileBasedPageModifiedTime();

        registeredExtraJavaScripts = siteConfig.registeredExtraJavaScripts;
        componentsRegistry = new WebSiteComponentsRegistry();
        resourceResolver = new ResourcesResolverChain();
        reactJsBundle = siteConfig.reactJsBundle;
        tocJavaScript = WebResource.withPath("toc.js");
        globalAssetsJavaScript = WebResource.withPath("assets.js");
        globalDocReferencesJavaScript = WebResource.withPath("documentation-references.js");
        searchIndexJavaScript = WebResource.withPath(SEARCH_INDEX_FILE_NAME);
        auxiliaryFilesRegistry = new AuxiliaryFilesRegistry();
        markupParsingConfiguration = MarkupParsingConfigurations.byName(cfg.documentationType);
        globalSearchEntries = new GlobalSearchEntries();
        localSearchEntries = new LocalSearchEntries();
        auxiliaryFilesLastUpdateTime = new HashMap<>();

        globalDocReferences = new GlobalDocReferences(componentsRegistry, cfg.globalReferencesPath);

        docMeta.setId(siteConfig.id);
        if (siteConfig.isPreviewEnabled) {
            docMeta.setPreviewEnabled(true);
        }

        componentsRegistry.setResourcesResolver(resourceResolver);

        localResourceResolver = new MultipleLocalLocationsResourceResolver(siteConfig.docRootPath);
        resourceResolver.addResolver(localResourceResolver);
        resourceResolver.addResolver(new ClassPathResourceResolver());
        resourceResolver.addResolver(new HttpBasedResourceResolver());
        resourceResolver.initialize(findLookupLocations(siteConfig));

        WebSiteResourcesProviders.add(new WebSiteLogoExtension(siteConfig.docRootPath));
        WebSiteResourcesProviders.add(new WebSiteGlobalOverridePlaceholderExtension());
        WebSiteResourcesProviders.add(initFileBasedWebSiteExtension(siteConfig));

        reset();
    }

    public Configuration getCfg() {
        return cfg;
    }

    public static Configuration withRoot(Path path) {
        Configuration configuration = new Configuration();
        configuration.withRootPath(path);

        return configuration;
    }

    public void regenerate() {
        reset();
        parseAndDeploy();
    }

    public AuxiliaryFilesRegistry getAuxiliaryFilesRegistry() {
        return auxiliaryFilesRegistry;
    }

    public Path getDeployRoot() {
        return cfg.deployPath;
    }

    public DocMeta getDocMeta() {
        return docMeta;
    }

    public ReactJsBundle getReactJsBundle() {
        return reactJsBundle;
    }

    public Map<String, Path> getOutsideDocsRequestedResources() {
        return localResourceResolver.getOutsideDocRequestedResources();
    }

    public void parseAndDeploy() {
        parse();
        deploy();
    }

    public void parse() {
        createTopLevelToc();
        createDocStructure();
        parseGlobalDocReference();
        validateTocItemsPresence();
        parseMarkupsMeta();
        parseMarkups();
        parseFooter();
        updateTocWithPageSections();
        validateCollectedLinks();
    }

    public void deploy() {
        reportPhase("deploying documentation");
        generatePages();
        generateSearchIndex();
        deployToc();
        deployGlobalAssets();
        deployGlobalDocReferences();
        deployAuxiliaryFiles();
        deployResources();
    }

    public TocItem tocItemByPath(Path path) {
        return markupParsingConfiguration.tocItemByPath(componentsRegistry, toc, path);
    }

    public HtmlPageAndPageProps regenerateAndValidatePageDeployTocAndAllPages(TocItem tocItem) {
        removeLinksForTocItem(tocItem);

        HtmlPageAndPageProps pageProps = regeneratePageOnly(tocItem);
        deployToc();

        docStructure.validateCollectedLinks();
        buildJsonOfAllPages();

        return pageProps;
    }

    public void removeLinksForTocItem(TocItem tocItem) {
        try {
            Path markupPath = markupPath(tocItem);

            docStructure.removeGlobalAnchorsForPath(markupPath);
            docStructure.removeLocalAnchorsForTocItem(tocItem);
            docStructure.removeLinksForPath(markupPath);
        } catch (UnresolvedResourceException e) {
            // ignored
        }
    }

    public HtmlPageAndPageProps regeneratePageOnly(TocItem tocItem) {
        parseMarkupAndUpdateTocItemAndSearch(tocItem);

        Page page = pageByTocItem.get(tocItem);
        tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());

        HtmlPageAndPageProps pageProps = generatePage(tocItem, page);

        auxiliaryFilesRegistry.auxiliaryFilesByTocItem(tocItem).stream()
                .filter(AuxiliaryFile::isDeploymentRequired)
                .forEach(this::deployAuxiliaryFileIfOutdated);

        return pageProps;
    }

    public Set<TocItem> dependentTocItems(Path auxiliaryFile) {
        return auxiliaryFilesRegistry.tocItemsByPath(auxiliaryFile);
    }

    public TableOfContents getToc() {
        return toc;
    }

    public TocAddedAndRemovedPages updateToc() {
        TableOfContents previousToc = toc;
        createTopLevelToc();

        docStructure.updateToc(toc);

        validateTocItemsPresence();

        List<TocItem> newTocItems = previousToc.detectNewTocItems(toc);
        List<TocItem> removedTocItems = previousToc.detectRemovedTocItems(toc);

        removedTocItems.forEach(this::removeLinksForTocItem);

        List<HtmlPageAndPageProps> newPages = newTocItems.stream().map(tocItem -> {
            parseMarkupAndUpdateTocItemAndSearch(tocItem);
            return regeneratePageOnly(tocItem);
        }).collect(toList());

        updateTocWithPageSections();

        docStructure.validateCollectedLinks();

        deployToc();

        return new TocAddedAndRemovedPages(toc, newPages, removedTocItems);
    }

    public DocReferences updateDocReferences() {
        docStructure.removeLinksForPath(globalDocReferences.getGlobalReferencesPath());

        globalDocReferences.load();
        deployGlobalDocReferences();

        validateCollectedLinks();

        return globalDocReferences.getDocReferences();
    }

    public void redeployAuxiliaryFileIfRequired(Path path) {
        if (auxiliaryFilesRegistry.requiresDeployment(path)) {
            deployAuxiliaryFile(auxiliaryFilesRegistry.auxiliaryFileByPath(path));
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
        extraJavaScriptsInFront.add(globalAssetsJavaScript);

        if (globalDocReferences.isPresent()) {
            extraJavaScriptsInFront.add(globalDocReferencesJavaScript);
        }

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
        toc = markupParsingConfiguration.createToc(componentsRegistry);
    }

    private void createDocStructure() {
        docStructure = new WebSiteDocStructure(componentsRegistry, docMeta, toc, markupParsingConfiguration);
        componentsRegistry.setDocStructure(docStructure);
    }

    private void parseGlobalDocReference() {
        reportPhase("parsing global doc references");
        globalDocReferences.load();
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

    private void deployGlobalAssets() {
        reportPhase("deploying global plugin assets");
        String globalAssetsJson = JsonUtils.serializePrettyPrint(componentsRegistry.globalAssetsRegistry().getAssets());
        deployer.deploy(globalAssetsJavaScript, "globalAssets = " + globalAssetsJson);
    }

    private void deployGlobalDocReferences() {
        if (!globalDocReferences.isPresent()) {
            return;
        }

        reportPhase("deploying global documentation references");

        String globalReferences = JsonUtils.serializePrettyPrint(globalDocReferences.getDocReferences().toMap());
        deployer.deploy(globalDocReferencesJavaScript, "docReferences = " + globalReferences);
    }

    private void validateTocItemsPresence() {
        reportPhase("validate TOC items presence");
        List<TocItem> missingTocItems = toc.getTocItems().stream()
                .filter(this::isTocItemMissing)
                .collect(toList());

        if (!missingTocItems.isEmpty()) {
            String renderedMissingTocItems = "    " + missingTocItems.stream()
                    .map(tocItem -> tocItem.toString() + ": can't find " +
                            markupParsingConfiguration.tocItemResourceName(tocItem))
                    .collect(Collectors.joining("\n    "));

            throw new RuntimeException("\nFollowing Table of Contents entries are missing associated files:\n\n" +
                    renderedMissingTocItems + "\n");
        }
    }

    private boolean isTocItemMissing(TocItem tocItem) {
        try {
            markupPath(tocItem);
        } catch (UnresolvedResourceException e) {
            return true;
        }

        return false;
    }

    private void parseMarkupsMeta() {
        reportPhase("parsing markup files meta");
        toc.getTocItems().forEach(this::parseMarkupMetaOnlyAndUpdateTocItem);
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

    private void parseMarkupMetaOnlyAndUpdateTocItem(TocItem tocItem) {
        Path markupPath = null;

        try {
            markupPath = markupPath(tocItem);
            PageMeta pageMeta = markupParser.parsePageMetaOnly(fileTextContent(markupPath));

            updateTocItemWithPageMeta(tocItem, pageMeta);
        } catch(Exception e) {
            throwParsingErrorMessage(tocItem, markupPath, e);
        }
    }

    private void parseMarkupAndUpdateTocItemAndSearch(TocItem tocItem) {
        Path markupPath = null;

        try {
            markupPath = markupPath(tocItem);
            Path relativePathToLog = cfg.docRootPath.relativize(markupPath);

            ConsoleOutputs.out("parsing ", Color.PURPLE, relativePathToLog);

            localResourceResolver.setCurrentFilePath(markupPath);

            MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
            updateFilesAssociation(tocItem, parserResult.getAuxiliaryFiles());

            Instant lastModifiedTime = pageModifiedTimeStrategy.lastModifiedTime(tocItem, markupPath);
            Page page = new Page(parserResult.getDocElement(), lastModifiedTime, parserResult.getPageMeta());
            pageByTocItem.put(tocItem, page);

            updateTocItemWithPageMeta(tocItem, page.getPageMeta());
            tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());

            updateSearchEntries(tocItem, parserResult);
        } catch(Exception e) {
            throwParsingErrorMessage(tocItem, markupPath, e);
        }
    }

    private static void throwParsingErrorMessage(TocItem tocItem, Path markupPath, Throwable e) {
        throw new RuntimeException("\nmarkup parsing error:\n" +
                "    TOC item: " + tocItem + "\n" +
                "    full path: " + markupPath + "\n" +
                "\n" + e.getMessage() + "\n", e);
    }

    private void updateTocItemWithPageMeta(TocItem tocItem, PageMeta pageMeta) {
        tocItem.setPageMeta(pageMeta);

        if (pageMeta.hasValue("title")) {
            tocItem.setPageTitle(pageMeta.getSingleValue("title"));
        }
    }

    private void updateSearchEntries(TocItem tocItem, MarkupParserResult parserResult) {
        List<GlobalSearchEntry> siteSearchEntries = parserResult.getSearchEntries().stream()
                .map(pageSearchEntry ->
                        new GlobalSearchEntry(
                                searchEntryUrl(tocItem, pageSearchEntry),
                                searchEntryTitle(tocItem, pageSearchEntry),
                                pageSearchEntry.getSearchText()))
                .collect(toList());

        globalSearchEntries.addAll(siteSearchEntries);
        localSearchEntries.add(new PageSearchEntries(tocItem, parserResult.getSearchEntries()));
    }

    private String searchEntryUrl(TocItem tocItem, PageSearchEntry pageSearchEntry) {
        DocUrl docUrl = tocItem.isIndex() ?
                DocUrl.indexUrl():
                new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), pageSearchEntry.getPageSectionId());

        return docStructure.createUrl(null, docUrl);
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
        return markupParsingConfiguration.fullPath(componentsRegistry, cfg.docRootPath, tocItem);
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

            Path originalPathForLogging = cfg.docRootPath.relativize(
                    markupParsingConfiguration.fullPath(componentsRegistry, cfg.docRootPath, tocItem).toAbsolutePath());

            deployer.deploy(originalPathForLogging, pagePath, html);

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

    private void deployAuxiliaryFileIfOutdated(AuxiliaryFile auxiliaryFile) {
        Long savedLastModifiedTime = auxiliaryFilesLastUpdateTime.get(auxiliaryFile);

        try {
            FileTime lastModifiedTime = Files.getLastModifiedTime(auxiliaryFile.getPath());
            if (savedLastModifiedTime != null && savedLastModifiedTime == lastModifiedTime.toMillis()) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        deployAuxiliaryFile(auxiliaryFile);
    }

    private void deployAuxiliaryFile(AuxiliaryFile auxiliaryFile) {
        try {
            deployer.deploy(auxiliaryFile.getDeployRelativePath(), Files.readAllBytes(auxiliaryFile.getPath()));
            FileTime lastModifiedTime = Files.getLastModifiedTime(auxiliaryFile.getPath());
            auxiliaryFilesLastUpdateTime.put(auxiliaryFile, lastModifiedTime.toMillis());
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

    private <E> Stream<E> mapEachTocItemWithoutPage(Function<TocItem, E> func) {
        return toc.getTocItems().stream()
                .filter(tocItem -> pageByTocItem.containsKey(tocItem))
                .map(func);
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
        private Path footerPath;
        private Path extensionsDefPath;
        private Path globalReferencesPath;
        private List<WebResource> webResources;
        private String id;
        private String title;
        private String type;
        private String fileWithLookupPaths;
        private String logoRelativePath;
        private List<WebResource> registeredExtraJavaScripts;
        private boolean isPreviewEnabled;
        private String documentationType = MARKDOWN;
        private DocMeta docMeta = new DocMeta(Collections.emptyMap());
        private ReactJsBundle reactJsBundle;
        private PageModifiedTimeStrategy pageModifiedTimeStrategy;

        private Configuration() {
            webResources = new ArrayList<>();
            registeredExtraJavaScripts = new ArrayList<>();
        }

        public Configuration withDocumentationType(String documentationType) {
            this.documentationType = documentationType;
            return this;
        }

        public Configuration withRootPath(Path path) {
            docRootPath = path.toAbsolutePath();
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

        // TODO add support for JSON
        // https://github.org/testingisdocumenting/znai/issues/339
        public Configuration withGlobalReferencesPath(Path path) {
            globalReferencesPath = path.toAbsolutePath();
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

        public Configuration withPageModifiedTimeStrategy(PageModifiedTimeStrategy modifiedTimeStrategy) {
            this.pageModifiedTimeStrategy = modifiedTimeStrategy;
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

        public Path getGlobalReferencesPath() {
            return globalReferencesPath;
        }

        public Path getDocRootPath() {
            return docRootPath;
        }

        public WebSite deployTo(Path path) {
            WebSite webSite = createWebSiteInstance(path);
            webSite.parseAndDeploy();

            return webSite;
        }

        public WebSite parseOnly() {
            WebSite webSite = createWebSiteInstance(Paths.get(""));
            webSite.parse();

            return webSite;
        }

        private WebSite createWebSiteInstance(Path path) {
            deployPath = path.toAbsolutePath();
            return new WebSite(this);
        }
    }
}
