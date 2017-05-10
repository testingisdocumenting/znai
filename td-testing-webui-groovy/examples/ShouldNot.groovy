import static com.twosigma.testing.webui.WebTestGroovyDsl.*
import static Pages.*

scenario("use `shouldNot` to negate any matcher") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.shouldNot == 1
}
