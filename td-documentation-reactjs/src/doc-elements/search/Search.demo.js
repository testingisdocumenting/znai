import React from "react"
import {getSearchPromise} from "./searchPromise"
import SearchPopup from "./SearchPopup"
import {elementsLibrary} from '../DefaultElementsLibrary'

const buttonStyle = {
    border: "1px solid",
    padding: 10,
    cursor: "pointer",
    width: 200
}

class SearchDemo extends React.Component {
    state = {isOpen: true}

    render() {
        const { isOpen } = this.state

        return isOpen ? (
            <SearchPopup searchPromise={getSearchPromise({id: ""})}
                         onClose={this.onClose}
                         elementsLibrary={elementsLibrary}/>
        ) : (
            <div style={buttonStyle} onClick={this.onShowClick}>Show Search</div>
        )
    }

    onClose = () => {
        this.setState({isOpen: false})
    }

    onShowClick = () => {
        this.setState({isOpen: true})
    }
}

export function searchPopupDemo(registry) {
    registry.add('search popup', <SearchDemo/>)
}