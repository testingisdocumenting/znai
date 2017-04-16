package com.twosigma.testing.webui.documentation.annotations;

import com.twosigma.testing.webui.page.PageElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.Map;

/**
 * @author mykola
 */
public class BadgeImageAnnotation extends ImageAnnotation {
    public BadgeImageAnnotation(PageElement pageElement, String text) {
        super(pageElement, "circle", text);
    }

    @Override
    public void addAnnotationData(Map<String, Object> data, WebElement webElement) {
        Point location = center(webElement);

        data.put("x", location.getX());
        data.put("y", location.getY());
        data.put("r", 20);
    }
}
