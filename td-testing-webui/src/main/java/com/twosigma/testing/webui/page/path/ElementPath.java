package com.twosigma.testing.webui.page.path;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mykola
 */
public class ElementPath {
    private List<ElementPathEntry> entries;

    public ElementPath() {
        entries = new ArrayList<>();
    }

    public void addSelector(ElementsSelector selector) {
        ElementPathEntry entry = new ElementPathEntry(selector);
        entries.add(entry);
    }

    public List<WebElement> find(WebDriver driver) {
        WebElement root = null;

        List<WebElement> webElements = Collections.emptyList();
        for (ElementPathEntry entry : entries) {
            webElements = entry.find(driver, root);
            if (webElements.isEmpty()) {
                return webElements;
            }

            root = webElements.get(0);
        }

        return webElements;
    }
}
