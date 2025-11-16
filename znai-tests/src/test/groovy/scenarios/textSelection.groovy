/*
 * Copyright 2025 znai maintainers
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

package scenarios

import docgen.ScaffoldUtils
import org.testingisdocumenting.webtau.WebTauGroovyDsl

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

def scaffoldServerUrl = cache.value('text-selection-scaffold-url')

scenario('scaffold docs for text selection test') {
    scaffoldServerUrl.set(
            ScaffoldUtils.scaffoldAndServe('text-selection-scaffold').baseUrl)
}

scenario('open docs in browser') {
    browser.open(scaffoldServerUrl.get() + '/my-product')
}

scenario('triple-click bullet point and generate link') {
    standardView.gettingStartedTocItem.click()
    standardView.pageTitle.waitTo == "Getting Started"

    def bulletElement = $("ul li").get(2)
    bulletElement.waitTo visible

    // TODO expose triple click and selenium actions via webtau
    def originalText = browser.driver.executeScript("""
        var elements = document.querySelectorAll('ul li');
        var element = elements[2];
        var range = document.createRange();
        range.selectNodeContents(element);
        var selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);

        var mouseUpEvent = new MouseEvent('mouseup', {
            view: window,
            bubbles: true,
            cancelable: true
        });
        element.dispatchEvent(mouseUpEvent);

        return selection.toString();
    """)

    def menu = $(".znai-text-selection-menu")
    menu.waitTo visible

    def generateLinkItem = $(".znai-text-selection-menu-item").get("Generate Link")
    generateLinkItem.waitTo visible
    generateLinkItem.click()

    def questionInput = $(".znai-text-selection-question-input")
    questionInput.waitTo visible

    questionInput.sendKeys("test")

    def sendButton = $(".znai-text-selection-send-button")
    sendButton.click()

    // TODO expose this via webtau
    def highlightUrl = browser.driver.executeAsyncScript("""
        var callback = arguments[arguments.length - 1];
        navigator.clipboard.readText().then(function(text) {
            callback(text);
        }).catch(function(err) {
            callback("Error reading clipboard: " + err);
        });
    """)

    browser.reopen(highlightUrl)
    def highlightedElement = $(".znai-highlight")
    highlightedElement.waitTo contain(originalText)
}
