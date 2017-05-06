import static com.twosigma.testing.webui.WebTestDsl.*

class SearchPage {
    def box = $('#search-box')

    void open() {
        open("/search")
        box.waitTo beVisible
    }

    void submit(value) {
        box.setValue(value)
    }
}