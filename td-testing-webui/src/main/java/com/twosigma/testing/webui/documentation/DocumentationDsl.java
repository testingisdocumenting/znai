package com.twosigma.testing.webui.documentation;

import com.twosigma.testing.webui.cfg.Configuration;
import com.twosigma.testing.webui.page.PageElement;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;
import org.openqa.selenium.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class DocumentationDsl {
    private Configuration cfg = Configuration.INSTANCE;

    private static final String BADGE = "circle";

    private TakesScreenshot screenshotTaker;
    private List<Annotation> annotations;
    private WebDriver driver;

    public DocumentationDsl(WebDriver driver) {
        this.driver = driver;
        this.screenshotTaker = (TakesScreenshot) driver;
    }

    private DocumentationDsl(WebDriver driver, List<Annotation> annotations) {
        this(driver);
        this.annotations = annotations;
    }

    public DocumentationDsl withAnnotations(Annotation... annotations) {
        return new DocumentationDsl(driver, assignDefaultText(Arrays.asList(annotations)));
    }

    public static Annotation badge(PageElement pageElement) {
        return new Annotation(BADGE, "", pageElement);
    }

    public void capture(String screenshotName) {
        createScreenshot(screenshotName);
        createAnnotations(screenshotName);
    }

    private void createScreenshot(String screenshotName) {
        Screenshot screenshot = new Screenshot(screenshotTaker);
        screenshot.save(cfg.getDocumentationArtifactsPath().resolve(screenshotName + ".png"));
    }

    private void createAnnotations(String screenshotName) {
        List<? extends Map<String, ?>> shapes = annotations.stream().map(this::createAnnotationData).collect(toList());

        Map<String, Object> result = new HashMap<>();
        result.put("shapes", shapes);
        result.put("pixelRatio", getPixelRatio());

        String annotationsJson = JsonUtils.serializePrettyPrint(result);
        FileUtils.writeTextContent(cfg.getDocumentationArtifactsPath().resolve(Paths.get(screenshotName + ".json")),
                annotationsJson);
    }

    private Map<String, ?> createAnnotationData(Annotation annotation) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", annotation.getId());
        data.put("type", annotation.getType());

        WebElement webElement = annotation.getPageElement().findElement();
        Point location = webElement.getLocation();
        data.put("x", location.getX());
        data.put("y", location.getY());
        data.put("r", 20);
        data.put("text", annotation.getText());
        data.put("color", "red");

        return data;
    }

    private List<Annotation> assignDefaultText(List<Annotation> annotations) {
        int badgeNumber = 0;
        for (Annotation annotation : annotations) {
            if (annotation.getType().equals(BADGE)) {
                badgeNumber++;
                annotation.setText(String.valueOf(badgeNumber));
            }
        }

        return annotations;
    }

    private Number getPixelRatio() {
        Object pixelRatio = ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
        return pixelRatio instanceof Number ? (Number) pixelRatio : 1;
    }
}
