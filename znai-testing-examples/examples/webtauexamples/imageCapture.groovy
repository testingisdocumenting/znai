package webtauexamples

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def homeSearchInput = $("#search_form_input_homepage")
def resultSearchInput = $("#search_form_input")
def searchButton = $("#search_button_homepage")
def result = $('article[data-testid="result"]')

scenario("capture screenshot") {
    browser.open("https://duckduckgo.com/")

    homeSearchInput.setValue("testing is documenting")
    searchButton.click()

    result.waitTo beVisible()

    browser.doc.withAnnotations(
            browser.doc.badge(resultSearchInput),
            browser.doc.badge(result)).capture("duckduckgo-search")
}