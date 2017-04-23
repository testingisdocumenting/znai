search = page('SearchPage')

scenario ("""# Search facts
You can enter a fact in a search box and fact will be displayed in a special box
""") {
    open("/search")
    search.submit("test")
}
