import static com.twosigma.testing.webui.WebTestDsl.*
import static Pages.*

scenario("""# Search facts
You can enter a fact in a search box and fact will be displayed in a special box
""") {
    search.open()
    search.submit("query")
    search.box.should == "query1"
}
