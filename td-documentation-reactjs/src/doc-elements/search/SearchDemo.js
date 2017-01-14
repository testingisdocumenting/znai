import React, {Component} from "react";
import Search from "./Search";
import SearchPopup from "./SearchPopup";
import testData from "./testData";

class SearchDemo extends Component {
    constructor(props) {
        super(props)

        this.search_ = new Search(testData.allPages, testData.searchIndex)
        this.queryResult = this.search_.search("external")
    }

    render() {
        return <SearchPopup search={this.search_}/>
    }
}

export default SearchDemo