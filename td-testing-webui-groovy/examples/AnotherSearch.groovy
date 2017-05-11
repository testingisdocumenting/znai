import static com.twosigma.testing.webui.WebTestGroovyDsl.*
import static pages.Pages.*

scenario("""# Alternative Search
to do different description
""") {
    search.open()
    search.box.should == "search this1"
}

scenario("""# Alternative Mega Search""") {
    search.open()
}
