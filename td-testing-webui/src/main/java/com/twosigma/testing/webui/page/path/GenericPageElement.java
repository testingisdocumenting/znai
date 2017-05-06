package com.twosigma.testing.webui.page.path;

import com.twosigma.testing.reporter.StepReporters;
import com.twosigma.testing.reporter.TestStep;
import com.twosigma.testing.reporter.TokenizedMessage;
import com.twosigma.testing.webui.page.ElementValue;
import com.twosigma.testing.webui.page.NullWebElement;
import com.twosigma.testing.webui.page.PageElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Supplier;

import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.TO;
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.action;
import static com.twosigma.testing.webui.reporter.WebUiMessageBuilder.stringValue;

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

    public void click() {
        execute(TokenizedMessage.build(action("clicking")),
                () -> TokenizedMessage.build(action("clicked")),
                () -> findElement().click());
    }

    public WebElement findElement() {
        List<WebElement> webElements = path.find(driver);
        return webElements.isEmpty() ?
                new NullWebElement(path.toString()) :
                webElements.get(0);
    }

    private void execute(TokenizedMessage inProgressMessage,
                         Supplier<TokenizedMessage> completionMessageSupplier,
                         Runnable action) {
        TestStep<PageElement> step = new TestStep<>(this, inProgressMessage);
        try {
            StepReporters.onStart(step);
            action.run();

            step.complete(completionMessageSupplier.get());
            StepReporters.onSuccess(step);
        } catch (Exception e) {
            step.fail(e);
            StepReporters.onFailure(step);
            throw e;
        }
    }

    @Override
    public ElementValue<?> elementValue() {
        return new ElementValue<>("value", this::getUnderlyingValue);
    }

    @Override
    public void setValue(Object value) {
        execute(TokenizedMessage.build(action("setting value"), stringValue(value), TO).add(pathDescription),
                () -> TokenizedMessage.build(action("set value"), stringValue(value), TO).add(pathDescription),
                () -> findElement().sendKeys(value.toString()));
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
}
