import static com.twosigma.testing.webui.WebTestGroovyDsl.*
import static pages.Pages.*

scenario("""# Search facts
You can enter a fact in a search box and 
fact will be displayed in a special box
""") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.waitTo == 2
}
