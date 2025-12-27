/*
 * Copyright 2020 znai maintainers
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

import org.testingisdocumenting.znai.core.*;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.console.ansi.FontStyle;
import org.testingisdocumenting.znai.extensions.PluginParamsWithDefaultsFactory;
import org.testingisdocumenting.znai.extensions.Plugins;
import org.testingisdocumenting.znai.preprocessor.RegexpBasedPreprocessor;
import org.testingisdocumenting.znai.resources.*;
import org.testingisdocumenting.znai.html.*;
import org.testingisdocumenting.znai.html.reactjs.ReactJsBundle;
import org.testingisdocumenting.znai.markdown.PageMarkdownSection;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.reference.GlobalDocReferences;
import org.testingisdocumenting.znai.search.*;
import org.testingisdocumenting.znai.structure.*;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;
import org.testingisdocumenting.znai.parser.MarkupParsingConfiguration;
import org.testingisdocumenting.znai.parser.MarkupParsingConfigurations;
import org.testingisdocumenting.znai.utils.ResourceUtils;
import org.testingisdocumenting.znai.website.modifiedtime.FileBasedPageModifiedTime;
import org.testingisdocumenting.znai.website.modifiedtime.PageModifiedTimeStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.parser.MarkupTypes.MARKDOWN;
import static org.testingisdocumenting.znai.utils.FileUtils.fileTextContent;
import static org.testingisdocumenting.znai.website.ProgressReporter.reportPhase;
import static java.util.stream.Collectors.toList;

public class WebSite implements Log {
    private static final String UPLOAD_FILE_NAME = "upload.txt";
    private static final String SEARCH_INDEX_FILE_NAME = "search-index.js";
    private final PluginParamsWithDefaultsFactory pluginParamsFactory;

    private final Set<TocChangeListener> tocChangeListeners;

    private PageToHtmlPageConverter pageToHtmlPageConverter;
    private MarkupParser markupParser;
    private final Deployer deployer;
    private final DocMeta docMeta;
    private final Configuration cfg;

    private Map<TocItem, Page> pageByTocItem;
    private Map<TocItem, MarkupParserResult> parserResultByTocItem;
    private final GlobalSearchEntries globalSearchEntries;
    private final LocalSearchEntries localSearchEntries;

    private TableOfContents toc;
    private final GlobalDocReferences globalDocReferences;
    private WebSiteDocStructure docStructure;
    private Footer footer;
    private Map<TocItem, DocPageReactProps> pagePropsByTocItem;
    private final List<WebResource> registeredExtraJavaScripts;
    private List<WebResource> extraJavaScriptsInFront;
    private List<WebResource> extraJavaScriptsInBack;

    private final WebSiteComponentsRegistry componentsRegistry;
    private final AuxiliaryFilesRegistry auxiliaryFilesRegistry;
    private final WebResource tocJavaScript;
    private final WebResource footerJavaScript;
    private final WebResource globalAssetsJavaScript;
    private final WebResource globalDocReferencesJavaScript;
    private final WebResource searchIndexJavaScript;

    private MultipleLocalLocationsResourceResolver localResourceResolver;
    private final ResourcesResolverChain resourceResolver;

    private final MarkupParsingConfiguration markupParsingConfiguration;

    private final Map<AuxiliaryFile, Long> auxiliaryFilesLastUpdateTime;

    private final PageModifiedTimeStrategy pageModifiedTimeStrategy;

    private final String llmUrlPrefix;

    private RegexpBasedPreprocessor regexpBasedPreprocessor;

    private WebSite(Configuration siteConfig) {
        cfg = siteConfig;
        deployer = new Deployer(siteConfig.docRootPath, siteConfig.deployPath);
        docMeta = siteConfig.docMeta;
        pageModifiedTimeStrategy = siteConfig.pageModifiedTimeStrategy != null ?
                siteConfig.pageModifiedTimeStrategy : new FileBasedPageModifiedTime();
        llmUrlPrefix = siteConfig.llmUrlPrefix;

        tocChangeListeners = new HashSet<>();

        registeredExtraJavaScripts = siteConfig.registeredExtraJavaScripts;
        componentsRegistry = new WebSiteComponentsRegistry(siteConfig.docRootPath, siteConfig.isValidateExternalLinks);
        componentsRegistry.setLog(this);
        pluginParamsFactory = new PluginParamsWithDefaultsFactory();
        componentsRegistry.setPluginParamsFactory(pluginParamsFactory);
        resourceResolver = new ResourcesResolverChain();
        tocJavaScript = WebResource.withPath("toc.js");
        footerJavaScript = WebResource.withPath("footer.js");
        globalAssetsJavaScript = WebResource.withPath("assets.js");
        globalDocReferencesJavaScript = WebResource.withPath("documentation-references.js");
        searchIndexJavaScript = WebResource.moduleWithPath(SEARCH_INDEX_FILE_NAME);
        auxiliaryFilesRegistry = new AuxiliaryFilesRegistry();
        markupParsingConfiguration = MarkupParsingConfigurations.byName(cfg.documentationType);
        globalSearchEntries = new GlobalSearchEntries();
        localSearchEntries = new LocalSearchEntries();
        auxiliaryFilesLastUpdateTime = new HashMap<>();

        globalDocReferences = new GlobalDocReferences(componentsRegistry, cfg.globalReferencesPathNoExt);

        docMeta.setId(siteConfig.id);
        if (siteConfig.isPreviewEnabled) {
            docMeta.setPreviewEnabled(true);
        }

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

    public Map<String, Path> getOutsideDocsRequestedResources() {
        return resourceResolver.getOutsideDocRequestedResources();
    }

    public void parseAndDeploy() {
        parse();
        deploy();
    }

    public void parse() {
        createResourceResolvers();
        registerPreprocessor();
        registerSiteResourceProviders();
        createTopLevelToc();
        createDocStructure();
        parseAndSetPluginGlobalParams();
        parseGlobalDocReference();
        resolveTocItemsPath();
        parseMarkupsMeta();
        parseMarkups();
        parseFooter();
        updateTocWithPageSections();
        validateCollectedLinks();
    }

    private void registerPreprocessor() {
        regexpBasedPreprocessor = resourceResolver.canResolve("preprocessor.csv") ?
                new RegexpBasedPreprocessor(resourceResolver.textContent("preprocessor.csv")) :
                new RegexpBasedPreprocessor(Collections.emptyList());
    }

    public void deploy() {
        reportPhase("deploying documentation");
        generatePages();
        generateChapterIndexRedirectPages();
        generatePageRedirects();
        generateSearchIndex();
        generateLlmContent();
        deployToc();
        deployFooter();
        deployMeta();
        deployGlobalAssets();
        deployGlobalDocReferences();
        deployAuxiliaryFiles();
        deployUploadFiles();
        deployResources();
        deployPluginsStats();
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
        MarkupPathWithError markupPathWithError = resolveTocItem(tocItem);
        if (markupPathWithError.path() == null) {
            return;
        }

        docStructure.removeGlobalAnchorsForPath(markupPathWithError.path());
        docStructure.removeLocalAnchorsForTocItem(tocItem);
        docStructure.removeLinksForPath(markupPathWithError.path());
    }

    public HtmlPageAndPageProps regeneratePageOnly(TocItem tocItem) {
        parseMarkupMetaOnlyAndUpdateTocItem(tocItem);
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

    public TocAddedUpdatedAndRemovedPages updateToc() {
        TableOfContents previousToc = toc;
        createTopLevelToc();

        docStructure.updateToc(toc);

        resolveTocItemsPath();

        List<TocItem> newTocItems = previousToc.detectNewTocItems(toc);
        List<TocItem> removedTocItems = previousToc.detectRemovedTocItems(toc);
        List<TocItem> changedTocItems = previousToc.detectChangedTocItems(toc);

        removedTocItems.forEach(this::removeLinksForTocItem);

        List<HtmlPageAndPageProps> addedOrUpdatedPages = Stream.concat(newTocItems.stream(), changedTocItems.stream()).map(tocItem -> {
            parseMarkupAndUpdateTocItemAndSearch(tocItem);
            return regeneratePageOnly(tocItem);
        }).collect(toList());

        updateTocWithPageSections();

        docStructure.validateCollectedLinks();

        deployToc();

        return new TocAddedUpdatedAndRemovedPages(toc, addedOrUpdatedPages, removedTocItems);
    }

    public DocReferences updateDocReferences() {
        docStructure.removeLinksForPath(globalDocReferences.getGlobalReferencesPathNoExt());

        globalDocReferences.load();
        deployGlobalDocReferences();

        validateCollectedLinks();

        return globalDocReferences.getDocReferences();
    }

    public FooterAndParseResult parseFooter() {
        Path markupPath = cfg.footerPath;

        if (! Files.exists(markupPath)) {
            return null;
        }

        reportPhase("parsing footer");

        localResourceResolver.setCurrentFilePath(markupPath);

        MarkupParserResult parserResult = markupParser.parse(markupPath, fileTextContent(markupPath));
        auxiliaryFilesRegistry.registerAdditionalAuxiliaryFiles(parserResult.auxiliaryFiles());

        footer = new Footer(parserResult.docElement());

        return new FooterAndParseResult(footer, parserResult);
    }

    public Footer updateFooter() {
        FooterAndParseResult footerAndParseResult = parseFooter();

        footerAndParseResult.parserResult().auxiliaryFiles().stream()
                .filter(AuxiliaryFile::isDeploymentRequired)
                .forEach(this::deployAuxiliaryFileIfOutdated);

        return footerAndParseResult.footer();
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
        pageToHtmlPageConverter = new PageToHtmlPageConverter(docMeta, ReactJsBundle.INSTANCE);
        markupParser = markupParsingConfiguration.createMarkupParser(componentsRegistry);
        pageByTocItem = new LinkedHashMap<>();
        parserResultByTocItem = new LinkedHashMap<>();
        pagePropsByTocItem = new LinkedHashMap<>();
        extraJavaScriptsInFront = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScriptsInFront.add(globalAssetsJavaScript);

        if (globalDocReferences.isPresent()) {
            extraJavaScriptsInFront.add(globalDocReferencesJavaScript);
        }

        extraJavaScriptsInFront.add(tocJavaScript);
        extraJavaScriptsInFront.add(footerJavaScript);
        extraJavaScriptsInBack = new ArrayList<>(registeredExtraJavaScripts);
        extraJavaScriptsInBack.add(searchIndexJavaScript);

        componentsRegistry.setDefaultParser(markupParser);
        componentsRegistry.setMarkdownParser(new MarkdownParser(componentsRegistry));
    }

    private void deployResources() {
        reportPhase("deploying resources");

        ReactJsBundle.INSTANCE.deploy(deployer);
        WebSiteResourcesProviders.cssResources().forEach(deployer::deploy);
        WebSiteResourcesProviders.jsResources().forEach(deployer::deploy);
        WebSiteResourcesProviders.jsClientOnlyResources().forEach(deployer::deploy);
        WebSiteResourcesProviders.additionalFilesToDeploy().forEach(deployer::deploy);
        cfg.webResources.forEach(deployer::deploy);
    }

    private void createTopLevelToc() {
        reportPhase("creating table of contents");
        toc = markupParsingConfiguration.createToc(docMeta.getTitle(), componentsRegistry);
    }

    private void createDocStructure() {
        docStructure = new WebSiteDocStructure(componentsRegistry, docMeta, toc, markupParsingConfiguration);
        componentsRegistry.setDocStructure(docStructure);
    }

    private void createResourceResolvers() {
        componentsRegistry.setResourcesResolver(resourceResolver);

        localResourceResolver = new MultipleLocalLocationsResourceResolver(toc, cfg.docRootPath);
        resourceResolver.addResolver(localResourceResolver);
        resourceResolver.addResolver(new ZipJarFileResourceResolver(this, cfg.docRootPath));
        resourceResolver.addResolver(new ClassPathResourceResolver());
        resourceResolver.addResolver(new HttpBasedResourceResolver());

        List<String> lookupLocations = findLookupLocations(cfg).toList();
        printLookupLocations(lookupLocations.stream());
        resourceResolver.initialize(lookupLocations.stream());
    }

    private void registerSiteResourceProviders() {
        WebSiteResourcesProviders.add(new WebSiteLogoExtension(cfg.docRootPath));
        WebSiteResourcesProviders.add(new WebSiteGlobalOverridePlaceholderExtension());
        WebSiteResourcesProviders.add(initFileBasedWebSiteExtension(cfg));
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
        forEachPage(((tocItem, page) -> {
            if (page == null) {
                if (tocItem.isIndex()) {
                    return;
                } else {
                    throw new IllegalStateException("no parsed page associated with " + tocItem);
                }
            }

            tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());
        }));
    }

    private void deployToc() {
        reportPhase("deploying table of contents");
        String tocJson = JsonUtils.serializePrettyPrint(toc.toListOfMaps());
        deployer.deploy(tocJavaScript, "toc = " + tocJson);
    }

    private void deployFooter() {
        reportPhase("deploying footer");
        String footerJson = footer != null ?
                JsonUtils.serializePrettyPrint(footer.toMap()):
                "undefined";

        deployer.deploy(footerJavaScript, "footer = " + footerJson);
    }

    private void deployMeta() {
        reportPhase("deploying meta");
        deployer.deploy(DocMeta.META_FILE_NAME, JsonUtils.serializePrettyPrint(docMeta.toMap()));
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

    private void resolveTocItemsPath() {
        reportPhase("validate TOC items");
        List<TocItem> missingTocItems = toc.resolveTocItemPathsAndReturnMissing(this::resolveTocItem);

        if (!missingTocItems.isEmpty()) {
            String renderedMissingTocItems = "    " + missingTocItems.stream()
                    .map(tocItem -> tocItem.toString() + ": can't find " +
                            markupParsingConfiguration.tocItemResourceName(tocItem))
                    .collect(Collectors.joining("\n    "));

            throw new RuntimeException("\nFollowing Table of Contents entries are missing associated files:\n\n" +
                    renderedMissingTocItems + "\n");
        }

        Collection<Path> tocItemPaths = toc.getResolvedPaths();
        tocChangeListeners.forEach(l -> l.onTocResolvedFiles(tocItemPaths));
    }

    private void parseMarkupsMeta() {
        reportPhase("parsing markup files meta");
        toc.getTocItems().forEach(this::parseMarkupMetaOnlyAndUpdateTocItem);
    }

    private void parseMarkups() {
        reportPhase("parsing markup files");
        toc.getTocItems().forEach(this::parseMarkupAndUpdateTocItemAndSearch);
    }

    private void parseMarkupMetaOnlyAndUpdateTocItem(TocItem tocItem) {
        MarkupPathWithError markupPathWithError = MarkupPathWithError.EMPTY;

        try {
            markupPathWithError = resolveTocItem(tocItem);
            if (markupPathWithError.path() == null && tocItem.isIndex()) {
                return;
            }

            if (markupPathWithError.error() != null) {
                throw markupPathWithError.error();
            }

            PageMeta pageMeta = markupParser.parsePageMetaOnly(fileTextContent(markupPathWithError.path()));
            updateTocItemWithPageMeta(tocItem, pageMeta);
        } catch(Exception e) {
            throwParsingErrorMessage(tocItem, markupPathWithError.path(), e);
        }
    }

    private void parseMarkupAndUpdateTocItemAndSearch(TocItem tocItem) {
        MarkupPathWithError markupPathWithError = MarkupPathWithError.EMPTY;

        try {
            markupPathWithError = resolveTocItem(tocItem);
            if (markupPathWithError.path() == null) {
                if (tocItem.isIndex()) {
                    return;
                }

                throw markupPathWithError.error();
            }

            Path relativePathToLog = cfg.docRootPath.relativize(markupPathWithError.path());

            ConsoleOutputs.out("parsing ", Color.PURPLE, relativePathToLog);

            localResourceResolver.setCurrentFilePath(markupPathWithError.path());

            String markupContent = fileTextContent(markupPathWithError.path());
            markupContent = regexpBasedPreprocessor.preprocess(markupContent);

            PageMeta pageMeta = markupParser.parsePageMetaOnly(markupContent);
            pluginParamsFactory.setPageLocalParams(pageMeta);

            MarkupParserResult parserResult = markupParser.parse(markupPathWithError.path(),
                    markupContent);
            updateFilesAssociation(tocItem, parserResult.auxiliaryFiles());

            Instant lastModifiedTime = pageModifiedTimeStrategy.lastModifiedTime(tocItem, markupPathWithError.path());
            Page page = new Page(parserResult.docElement(), lastModifiedTime, parserResult.pageMeta());
            pageByTocItem.put(tocItem, page);
            parserResultByTocItem.put(tocItem, parserResult);

            updateTocItemWithPageMeta(tocItem, page.getPageMeta());
            tocItem.setPageSectionIdTitles(page.getPageSectionIdTitles());

            updateSearchEntries(tocItem, parserResult);
        } catch(Exception e) {
            throwParsingErrorMessage(tocItem, markupPathWithError.path(), e);
        } finally {
            localResourceResolver.setCurrentFilePath(null);
        }
    }

    private static void throwParsingErrorMessage(TocItem tocItem, Path markupPath, Throwable e) {
        throw new RuntimeException("\nmarkup parsing error:\n" +
                "    TOC item: " + tocItem + "\n" +
                "    full path: " + markupPath + "\n" +
                "\n" + e.getMessage(), e);
    }

    private void updateTocItemWithPageMeta(TocItem tocItem, PageMeta pageMeta) {
        tocItem.setPageMeta(pageMeta);

        if (pageMeta.hasValue("title")) {
            tocItem.setPageTitleIfNoTocOverridePresent(pageMeta.getSingleValue("title"));
        } else if (tocItem.isIndex()) {
            tocItem.setPageTitleIfNoTocOverridePresent("");
        }
    }

    private void updateSearchEntries(TocItem tocItem, MarkupParserResult parserResult) {
        List<GlobalSearchEntry> siteSearchEntries = parserResult.markdown().sections().stream()
                .filter(section -> !(section.title().isEmpty() && section.markdown().trim().isEmpty()))
                .map(section -> new GlobalSearchEntry(
                        docMeta,
                        tocItem,
                        section,
                        searchEntryUrl(tocItem, section)))
                .collect(toList());

        globalSearchEntries.addAll(siteSearchEntries);
        localSearchEntries.add(new PageLocalSearchEntries(tocItem, parserResult.searchEntries()));
    }

    private String searchEntryUrl(TocItem tocItem, PageMarkdownSection section) {
        DocUrl docUrl = new DocUrl(tocItem.getDirName(), tocItem.getFileNameWithoutExtension(), section.id());
        return docStructure.createUrl(null, docUrl);
    }

    // each markup file may refer other files like code snippets or diagrams
    // we maintain dependency between them, so we know which one triggers what page refresh during preview mode
    //
    private void updateFilesAssociation(TocItem tocItem, List<AuxiliaryFile> newAuxiliaryFiles) {
        newAuxiliaryFiles.forEach((af) -> auxiliaryFilesRegistry.updateFileAssociations(tocItem, af));
    }

    private MarkupPathWithError resolveTocItem(TocItem tocItem) {
        try {
            return new MarkupPathWithError(
                    markupParsingConfiguration.fullPath(componentsRegistry, cfg.docRootPath, tocItem),
                    null);
        } catch (UnresolvedResourceException e) {
            return new MarkupPathWithError(null, e);
        }
    }

    private void generatePages() {
        reportPhase("generating the rest of HTML pages");
        forEachPage(this::generatePage);
        buildJsonOfAllPages();
    }

    private void generateChapterIndexRedirectPages() {
        reportPhase("generating chapter index redirect pages");

        Set<String> dirNames = toc.getAllDirNames();
        dirNames.forEach(dirName -> {
            toc.firstPageInChapter(dirName).ifPresent((tocItem) -> {
                String redirectUrl = docStructure.fullUrl(
                        tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension());
                String redirectPage = ResourceUtils.textContent("template/redirect.html")
                        .replace("${newUrl}", redirectUrl);

                deployer.deploy(dirName + "/index.html", redirectPage);
            });
        });
    }

    private void generatePageRedirects() {
        PageRedirects pageRedirects = new PageRedirects(docStructure, deployer, cfg.redirectsPaths);
        if (!pageRedirects.isPresent()) {
            return;
        }

        reportPhase("generating page redirects");
        pageRedirects.deployRedirectPages();
    }

    private void generateSearchIndex() {
        reportPhase("generating search index");

        String jsIndexScript = localSearchEntries.buildIndexScript();
        deployer.deploy("search-index.js", jsIndexScript);

        String xmlExternalIndex = globalSearchEntries.toXml();
        deployer.deploy("search-entries.xml", xmlExternalIndex);
    }

    private void generateLlmContent() {
        reportPhase("generating LLM context file");

        LlmContentGenerator generator = new LlmContentGenerator(
                docMeta,
                llmUrlPrefix,
                pageByTocItem,
                parserResultByTocItem
        );

        String llmContent = generator.generateContent();
        deployer.deploy("llm.txt", llmContent);
    }

    public void buildJsonOfAllPages() {
        List<Map<String, ?>> listOfMaps = this.pagePropsByTocItem
                .values().stream().map(DocPageReactProps::toMap).collect(toList());
        String json = JsonUtils.serialize(listOfMaps);

        deployer.deploy("all-pages.json", json);
    }

    private HtmlPageAndPageProps generatePage(TocItem tocItem, Page page) {
        try {
            HtmlPageAndPageProps htmlAndProps = createHtmlPageAndProps(tocItem, page);

            pagePropsByTocItem.put(tocItem, htmlAndProps.props());
            extraJavaScriptsInFront.forEach(htmlAndProps.htmlPage()::addJavaScriptInFront);
            extraJavaScriptsInBack.forEach(htmlAndProps.htmlPage()::addJavaScript);

            String html = htmlAndProps.htmlPage().render(docMeta.getId());

            Path pagePath = tocItem.isIndex() ? Paths.get("index.html") :
                    Paths.get(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension()).resolve("index.html");

            MarkupPathWithError markupPathWithError = resolveTocItem(tocItem);

            if (markupPathWithError.path() == null && tocItem.isIndex()) {
                deployer.deploy("auto-generated-index", pagePath, html);
            } else {
                Path originalPathForLogging = cfg.docRootPath.relativize(
                        markupParsingConfiguration.fullPath(componentsRegistry, cfg.docRootPath, tocItem).toAbsolutePath());

                deployer.deploy(originalPathForLogging.toString(), pagePath, html);
            }

            return htmlAndProps;
        } catch (Exception e) {
            throw new RuntimeException("error during rendering of " +
                    tocItem.getFileNameWithoutExtension() + ": " + e.getMessage(),
                    e);
        }
    }

    private HtmlPageAndPageProps createHtmlPageAndProps(TocItem tocItem, Page page) {
        if (page == null && tocItem.isIndex()) {
            return createRedirectingToFirstTocItemHtmlPageAndProps();
        }

        return pageToHtmlPageConverter.convert(
                tocItem, page, createServerSideRenderer(tocItem));
    }

    private HtmlPageAndPageProps createRedirectingToFirstTocItemHtmlPageAndProps() {
        TocItem firstNonIndexPage = toc.firstNonIndexPage();

        if (firstNonIndexPage == null) {
            throw new IllegalStateException("no documentation pages found");
        }

        String url = firstNonIndexPage.getDirName() + "/" + firstNonIndexPage.getFileNameWithoutExtension();
        MarkupParserResult parserResult = markupParser.parse(Paths.get("index"),
                ":include-redirect: " + url);

        Instant lastModifiedTime = Instant.ofEpochSecond(0);
        Page page = new Page(parserResult.docElement(), lastModifiedTime, parserResult.pageMeta());

        return pageToHtmlPageConverter.convert(
                toc.getIndex(), page, createServerSideRenderer(firstNonIndexPage));
    }

    private RenderSupplier createServerSideRenderer(TocItem tocItem) {
        PageLocalSearchEntries pageSearchEntries = localSearchEntries.searchEntriesByTocItem(tocItem);

        return () -> ServerSideSimplifiedRenderer.renderPageTextContent(pageSearchEntries) +
                ServerSideSimplifiedRenderer.renderToc(toc, docMeta.getId());
    }

    private void deployAuxiliaryFiles() {
        reportPhase("deploying auxiliary files (e.g. images)");
        auxiliaryFilesRegistry.getAuxiliaryFilesForDeployment().forEach(this::deployAuxiliaryFile);
    }

    private void deployUploadFiles() {
        reportPhase("deploying files from " + UPLOAD_FILE_NAME);

        Path uploadTxtPath = cfg.getDocRootPath().resolve(UPLOAD_FILE_NAME);
        if (!Files.exists(uploadTxtPath)) {
            return;
        }

        String fileNames = fileTextContent(uploadTxtPath);
        String[] names = fileNames.split("\n");
        Arrays.stream(names)
                .map(String::trim)
                .forEach((uploadEntry) -> {
                    Path fullPathToUpload = resourceResolver.fullPath(uploadEntry);
                    deployer.deployFile(fullPathToUpload, uploadEntry);
                });
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

    private void deployPluginsStats() {
        reportPhase("deploying plugins statistics");
        deployer.deploy("plugin-stats.json", JsonUtils.serialize(Plugins.buildStatsMap()));
    }

    private void forEachPage(PageConsumer consumer) {
        toc.getTocItems().forEach(tocItem -> {
            Page page = pageByTocItem.get(tocItem);
            consumer.consume(tocItem, page);
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

        Stream<String> additionalLookupPathsStream = cfg.additionalLookupPaths != null ?
                cfg.additionalLookupPaths.stream() :
                Stream.empty();

        return Stream.concat(root,
                Stream.concat(additionalLookupPathsStream,
                        readLocationsFromFile(cfg.fileWithLookupPaths)));
    }

    private void printLookupLocations(Stream<String> stream) {
        reportPhase("lookup locations:");
        stream.forEach((path) -> ConsoleOutputs.out("    ", Color.PURPLE, path));
    }

    private void parseAndSetPluginGlobalParams() {
        if (!Files.exists(cfg.pluginParamsPath)) {
            return;
        }

        reportPhase("reading plugin global parameters:");
        ConsoleOutputs.out(Color.BLUE, "path: ", Color.PURPLE, cfg.pluginParamsPath);

        Map<String, Map<String, ?>> globalPluginParams = readPluginParams(cfg.pluginParamsPath);
        pluginParamsFactory.setGlobalParams(globalPluginParams);
        printPluginDefaultParams(globalPluginParams);
    }

    private void printPluginDefaultParams(Map<String, Map<String, ?>> params) {
        for (Map.Entry<String, Map<String, ?>> pluginEntry : params.entrySet()) {
            String pluginId = pluginEntry.getKey();
            if (!Plugins.hasPlugin(pluginId)) {
                throw new IllegalArgumentException("no plugin found with id: " + pluginId);
            }

            Map<String, ?> pluginParams = pluginEntry.getValue();

            List<Object> messageParts = new ArrayList<>(Arrays.asList(Color.YELLOW, pluginId, ": "));
            for (Map.Entry<String, ?> paramEntry : pluginParams.entrySet()) {
                messageParts.add(Color.PURPLE);
                messageParts.add(paramEntry.getKey());
                messageParts.add(":");
                messageParts.add(FontStyle.NORMAL);
                messageParts.add(paramEntry.getValue());
                messageParts.add(" ");
            }

            ConsoleOutputs.out(messageParts.toArray());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, ?>> readPluginParams(Path pluginParamsPath) {
        if (!Files.exists(pluginParamsPath)) {
            return Collections.emptyMap();
        }

        Map<String, ?> pluginParams = JsonUtils.deserializeAsMap(fileTextContent(pluginParamsPath));
        for (Object params : pluginParams.values()) {
            if (!(params instanceof Map)) {
                throw new IllegalArgumentException("expected plugin parameters to be an object, given: " + params.getClass().getSimpleName());
            }
        }

        return (Map<String, Map<String, ?>>) pluginParams;
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

    @Override
    public void phase(String message) {
        reportPhase(message);
    }

    @Override
    public void info(Object... styleOrValue) {
        ConsoleOutputs.out(styleOrValue);
    }

    @Override
    public void warn(Object... styleOrValue) {
        ConsoleOutputs.out(Stream.concat(Stream.of(Color.YELLOW, "[Warning] ", FontStyle.NORMAL), Arrays.stream(styleOrValue)).toArray());
    }

    public void registerTocChangeListener(TocChangeListener listener) {
        tocChangeListeners.add(listener);
    }

    public void unregisterTocChangeListener(TocChangeListener listener) {
        tocChangeListeners.remove(listener);
    }

    private interface PageConsumer {
        void consume(TocItem tocItem, Page page);
    }

    public static class Configuration {
        private Path deployPath;
        private Path docRootPath;
        private Path footerPath;
        private Path extensionsDefPath;
        private Path redirectsPaths;
        private Path globalReferencesPathNoExt;
        private Path pluginParamsPath;
        private final List<WebResource> webResources;
        private String id;
        private String title;
        private String type;
        private String fileWithLookupPaths;
        private final List<WebResource> registeredExtraJavaScripts;
        private boolean isPreviewEnabled;
        private boolean isValidateExternalLinks;
        private String documentationType = MARKDOWN;
        private DocMeta docMeta = new DocMeta(Collections.emptyMap());
        private PageModifiedTimeStrategy pageModifiedTimeStrategy;
        private List<String> additionalLookupPaths;
        private String llmUrlPrefix;

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

        public Configuration withExtensionsDefPath(Path path) {
            extensionsDefPath = path.toAbsolutePath();
            return this;
        }

        public Configuration withRedirectsPath(Path path) {
            redirectsPaths = path.toAbsolutePath();
            return this;
        }

        public Configuration withGlobalReferencesPathNoExt(Path path) {
            globalReferencesPathNoExt = path.toAbsolutePath();
            return this;
        }

        public Configuration withGlobalPluginParamsPath(Path path) {
            pluginParamsPath = path.toAbsolutePath();
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

        public Configuration withAdditionalLookupPaths(List<String> additionalLookupPaths) {
            this.additionalLookupPaths = additionalLookupPaths;
            return this;
        }

        public Configuration withLlmUrlPrefix(String llmUrlPrefix) {
            this.llmUrlPrefix = llmUrlPrefix;
            return this;
        }

        public Configuration withMetaFromJsonFile(Path path) {
            String json = fileTextContent(path);
            docMeta = new DocMeta(json);
            attachSlackMetaFromEnvVars(docMeta);

            withTitle(docMeta.getTitle());
            withType(docMeta.getType());

            return this;
        }

        private void attachSlackMetaFromEnvVars(DocMeta docMeta) {
            addMetaFromEnvVar(docMeta, "sendToSlackUrl", "ZNAI_SEND_TO_SLACK_URL");
            addMetaFromEnvVar(docMeta, "slackChannel", "ZNAI_SLACK_CHANNEL");
            addMetaFromEnvVar(docMeta, "slackActiveQuestionsUrl", "ZNAI_SLACK_ACTIVE_QUESTIONS_URL");
            addMetaFromEnvVar(docMeta, "resolveSlackQuestionUrl", "ZNAI_RESOLVE_SLACK_QUESTION_URL");
            addMetaFromEnvVar(docMeta, "sendToSlackIncludeContentType", "ZNAI_SEND_TO_SLACK_INCLUDE_CONTENT_TYPE");
            addMetaFromEnvVar(docMeta, "trackActivityUrl", "ZNAI_TRACK_ACTIVITY_URL");
            addMetaFromEnvVar(docMeta, "trackActivityIncludeContentType", "ZNAI_TRACK_ACTIVITY_INCLUDE_CONTENT_TYPE");
        }

        private void addMetaFromEnvVar(DocMeta docMeta, String key, String envVarName) {
            String envValue = System.getenv(envVarName);
            if (envValue != null) {
                docMeta.addMetaIfNotPresent(key, envValue);
            }
        }

        public Configuration withEnabledPreview(boolean isPreviewEnabled) {
            this.isPreviewEnabled = isPreviewEnabled;
            return this;
        }

        public Configuration withValidateExternalLinks(boolean isValidateExternalLinks) {
            this.isValidateExternalLinks = isValidateExternalLinks;
            return this;
        }

        public Path getGlobalReferencesPathNoExt() {
            return globalReferencesPathNoExt;
        }

        public Path getDocRootPath() {
            return docRootPath;
        }

        public Path getFooterPath() {
            return footerPath;
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
