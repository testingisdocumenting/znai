package com.twosigma.testing.webui.documentation;

import org.openqa.selenium.TakesScreenshot;

/**
 * @author mykola
 */
public class DocumentationDsl {
    private TakesScreenshot screenshotTaker;

    public DocumentationDsl(TakesScreenshot screenshotTaker) {
        this.screenshotTaker = screenshotTaker;
    }

    public void capture(String screenshotName) {
        Screenshot screenshot = new Screenshot(screenshotTaker);
        screenshot.save(screenshotName);
    }
}
