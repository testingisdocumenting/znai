package webtauexamples

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def homeSearchInput = $('input[class*="searchbox_input"]')
def resultSearchInput = $("#search_form_input")
def result = $('article[data-testid="result"]')

scenario("capture screenshot") {
    browser.open("https://duckduckgo.com/")

    homeSearchInput.waitToBe visible
    homeSearchInput.setValue("testing is documenting")
    homeSearchInput.sendKeys(browser.keys.enter)

    result.waitTo visible

    browser.doc.withAnnotations(resultSearchInput, result)
            .capture("duckduckgo-search")
}