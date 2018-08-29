import React, {Component} from 'react'

class SearchBox extends Component {
    constructor(props) {
        super(props)
        this.state = {value: ""}
    }

    render() {
        return (
            <div className="search-box">
                <input
                    ref={(dom) => this.dom = dom}
                    placeholder="Type to terms to search..."
                    onKeyDown={this.onKeyDown}
                    value={this.state.value}
                    onChange={this.onInputChange}/>
            </div>
        )
    }

    componentDidMount() {
        this.dom.focus();
    }

    // TODO debounce?
    onInputChange = (e) => {
        const value = e.target.value
        this.props.onChange(value)
        this.setState({value})
    }

    onKeyDown = (e) => {
        if (e.key === 'ArrowUp' || e.key === 'ArrowDown') {
            e.preventDefault()
        }
    }
}

export default SearchBox
