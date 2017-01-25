import React, {Component} from 'react'

class Preview extends Component {
    render() {
        const {active} = this.props // TODO state after connection
        return active ? (<div className='glyphicon glyphicon-eye-open preview-icon'></div>) : null
    }
}

export default Preview