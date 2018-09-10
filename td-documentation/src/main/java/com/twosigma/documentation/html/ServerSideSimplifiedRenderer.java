package com.twosigma.documentation.html;

import com.twosigma.documentation.parser.PageSectionIdTitle;
import com.twosigma.documentation.search.PageSearchEntries;
import com.twosigma.documentation.search.PageSearchEntry;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;

import java.util.stream.Collectors;

public class ServerSideSimplifiedRenderer {
    public static String renderToc(TableOfContents toc) {
        return section(
                toc.getTocItems().stream()
                        .map(ServerSideSimplifiedRenderer::renderTocLink)
                        .collect(Collectors.joining("\n")));
    }

    public static String renderPageTextContent(PageSearchEntries pageSearchEntries) {
        return section(
                pageSearchEntries.getEntries().stream()
                        .map(ServerSideSimplifiedRenderer::renderPageEntry)
                        .collect(Collectors.joining("\n")));
    }

    private static String renderTocLink(TocItem tocItem) {
        String rootLink = aHref(
                tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension() + "/",
                tocItem.getPageTitle());

        String subLinks = tocItem.getPageSectionIdTitles().stream()
                .map(section -> renderTocSubLink(tocItem, section))
                .collect(Collectors.joining(""));

        return article(rootLink + subLinks);
    }

    private static String renderTocSubLink(TocItem tocItem, PageSectionIdTitle pageSectionIdTitle) {
        return aHref(
                tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension() + "/#" + pageSectionIdTitle.getId(),
                tocItem.getPageTitle() + " " + pageSectionIdTitle.getTitle());
    }

    private static String renderPageEntry(PageSearchEntry entry) {
        String optionalHeader = entry.getPageSectionTitle().isEmpty() ?
                "" :
                "<header><h1>" + entry.getPageSectionTitle() + "</h1></header>\n";

        String paragraph = "<p>" + entry.getSearchText().getText() + "</p>\n";

        return article(optionalHeader + paragraph);
    }

    private static String section(String htmlBlock) {
        return "<section style=\"max-width: 640px; margin-left: auto; margin-right: auto;\">\n" +
                htmlBlock +
                "</section>\n";
    }

    private static String article(String htmlBlock) {
        return "<article>\n" + htmlBlock + "</article>\n";
    }

    private static String aHref(String url, String title) {
        return "<a href=\"" + url + "\">" + title + "</a>\n";
    }
}
