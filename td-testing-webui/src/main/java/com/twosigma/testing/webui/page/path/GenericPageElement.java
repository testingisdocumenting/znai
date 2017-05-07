package com.twosigma.testing.webui.page.path;

import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webui.page.ElementValue;
import com.twosigma.testing.webui.page.NullWebElement;
import com.twosigma.testing.webui.page.PageElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Supplier;

import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.testing.webui.WebTestDsl.executeStep;
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.*;

/**
 * @author mykola
 */
public class GenericPageElement implements PageElement {
    private boolean isMultipleElements;
    private WebDriver driver;
    private ElementPath path;
    private final TokenizedMessage pathDescription;
    private ElementValue<String> elementValue;

    public GenericPageElement(WebDriver driver, ElementPath path) {
        this.driver = driver;
        this.path = path;
        this.pathDescription = path.toTokenizedMessage();
        this.elementValue = new ElementValue<>("value", this::fetchValue);
    }

    public PageElement all() {
        GenericPageElement element = new GenericPageElement(driver, path);
        element.isMultipleElements = true;

        return element;
    }

    @Override
    public TokenizedMessage describe() {
        return pathDescription;
    }

    public void click() {
        execute(tokenizedMessage(action("clicking")).add(pathDescription),
                () -> tokenizedMessage(action("clicked")).add(pathDescription),
                () -> findElement().click());
    }

    public WebElement findElement() {
        List<WebElement> webElements = path.find(driver);
        return webElements.isEmpty() ?
                new NullWebElement(path.toString()) :
                webElements.get(0);
    }

    @Override
    public ElementValue<?> elementValue() {
        return new ElementValue<>("value", this::getUnderlyingValue);
    }

    @Override
    public void setValue(Object value) {
        execute(tokenizedMessage(action("setting value"), stringValue(value), TO).add(pathDescription),
                () -> tokenizedMessage(action("set value"), stringValue(value), TO).add(pathDescription),
                () -> {
                    clear();
                    sendKeys(value.toString());
                });
    }

    @Override
    public void sendKeys(String keys) {
        execute(tokenizedMessage(action("sending keys"), stringValue(keys), TO).add(pathDescription),
                () -> tokenizedMessage(action("sent value"), stringValue(keys), TO).add(pathDescription),
                () -> findElement().sendKeys(keys));
    }

    @Override
    public void clear() {
        execute(tokenizedMessage(action("clearing")).add(pathDescription),
                () -> tokenizedMessage(action("cleared")).add(pathDescription),
                () -> findElement().clear());
    }

    @Override
    public boolean isVisible() {
        return findElement().isDisplayed();
    }

    private String fetchValue() {
        return findElement().getText();
    }

    private String getText() {
        return findElement().getText();
    }

    private String getTagName() {
        return findElement().getTagName();
    }

    private String getAttribute(String name) {
        return findElement().getAttribute(name);
    }

    private Object getUnderlyingValue() {
        String tagName = getTagName().toUpperCase();
        return (tagName.equals("INPUT") || tagName.equals("TEXTAREA")) ?
                getAttribute("value") : getText();
    }

    @Override
    public String toString() {
        return path.toString();
    }

    private void execute(TokenizedMessage inProgressMessage,
                         Supplier<TokenizedMessage> completionMessageSupplier,
                         Runnable action) {
        executeStep(this, inProgressMessage, completionMessageSupplier, action);
    }
}
