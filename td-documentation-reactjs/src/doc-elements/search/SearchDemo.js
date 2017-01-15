import React, {Component} from "react"
import {getSearchPromise} from "./searchPromise"
import SearchPopup from "./SearchPopup"

class SearchDemo extends Component {
    render() {
        return <SearchPopup searchPromise={getSearchPromise()}/>
    }
}

export default SearchDemo