import React, {Component} from "react"
import {getSearchPromise} from "./searchPromise"
import SearchPopup from "./SearchPopup"
import LunrIndexer from "./LunrIndexer"
import testData from "./LunrIndexer.testdata"

const allPages = LunrIndexer.createWithPages(testData.allPages)

class SearchDemo extends Component {
    render() {
        return <SearchPopup searchPromise={getSearchPromise({id: ""})}/>
    }
}

export default SearchDemo