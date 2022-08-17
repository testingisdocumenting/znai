package webtauexamples

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def homeSearchInput = $('input[id*="search"]')
def resultSearchInput = $("#search_form_input")
def result = $('article[data-testid="result"]')

scenario("capture screenshot") {
    browser.open("https://duckduckgo.com/")

    homeSearchInput.waitToBe visible
    homeSearchInput.setValue("testing is documenting\n")

    result.waitTo visible

    browser.doc.withAnnotations(resultSearchInput, result)
            .capture("duckduckgo-search")
}