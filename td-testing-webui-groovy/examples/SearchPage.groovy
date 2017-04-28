import static com.twosigma.testing.webui.WebTestDsl.*

class SearchPage {
    def box = $('#search-box')

    void open() {
        open("/search")
    }

    void submit(value) {
        this.box.setValue(value)
    }
}