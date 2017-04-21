def search = page('SearchPage.groovy')

scenario ("""# Search facts
You can enter a fact in a search box and fact will be displayed in a special box
""") {
    open("/search")
    search.box.setValue("test")
}
