package org.testingisdocumenting.znai.website;

import org.testingisdocumenting.znai.html.Deployer;
import org.testingisdocumenting.znai.parser.table.CsvTableParser;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PageRedirects {
    private final Deployer deployer;
    private final Path csvPath;
    private final DocStructure docStructure;

    public record FromTo (String oldLink, String newDirName, String newFileNameWithoutExtension) {}

    public PageRedirects(DocStructure docStructure, Deployer deployer, Path csvPath) {
        this.docStructure = docStructure;
        this.deployer = deployer;
        this.csvPath = csvPath;
    }

    public boolean isPresent() {
        return Files.exists(csvPath);
    }

    public void deployRedirectPages() {
        List<FromTo> redirects = parse(csvPath);
        redirects.forEach(this::deployRedirect);
    }

    private void deployRedirect(FromTo fromTo) {
        TocItem tocItem = docStructure.tableOfContents().findTocItem(fromTo.newDirName, fromTo.newFileNameWithoutExtension);
        if (tocItem == null) {
            throw new RuntimeException("toc item not found: " +
                    fromTo.newDirName + "/" + fromTo.newFileNameWithoutExtension);
        }

        String redirectUrl = docStructure.fullUrl(
                tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension());
        String redirectPage = ResourceUtils.textContent("template/redirect.html")
                .replace("${newUrl}", redirectUrl);
        deployer.deploy(fromTo.oldLink + "/index.html", redirectPage);
    }

    private static List<FromTo> parse(Path csvPath) {
        return parse(FileUtils.fileTextContent(csvPath));
    }

    protected static List<FromTo> parse(String content) {
        String withoutComments = Arrays.stream(content.split("\n"))
                .filter(line -> !line.startsWith("#"))
                .collect(Collectors.joining("\n"));

        MarkupTableData tableData = CsvTableParser.parseWithHeader(withoutComments, "reference", "url");

        List<FromTo> result = new ArrayList<>();
        tableData.forEachRow(row -> {
            String newUrl = row.get(1).toString();
            String[] parts = newUrl.split("/");
            String newDirName;
            String newFileNameWithoutExtension;
            if (parts.length == 1) {
                newDirName = "";
                newFileNameWithoutExtension = parts[0];
            } else if (parts.length == 2) {
                newDirName = parts[0];
                newFileNameWithoutExtension = parts[1];
            } else {
                throw new RuntimeException("invalid url format, expected [dirName/]fileName");
            }

            result.add(new FromTo(row.get(0), newDirName, newFileNameWithoutExtension));
        });

        return result;
    }
}
