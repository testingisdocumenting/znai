page {
    box = $('#search-box')

    submit = action('submitting search query $value') { value ->
        searchBox.setValue(value)
    }
}