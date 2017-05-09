import static com.twosigma.testing.webui.WebTestGroovyDsl.*

class SearchPage {
    def box = $('#search-box')
    def numberOfResults = $('#results .result').count

    void open() {
        open("/search")
        box.waitTo beVisible
    }

    def submit = action("submitting search value '<query>'") {
        box.setValue(it.query)
        box.sendKeys("\n")
    }
}