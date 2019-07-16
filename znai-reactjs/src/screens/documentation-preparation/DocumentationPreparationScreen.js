import * as React from 'react'

import DocumentationPreparation from './DocumentationPreparation'
import {socketUrl} from '../../utils/socket'

import './DocumentationPreparationScreen.css'

export class DocumentationPreparationScreen extends React.Component {
    constructor(props) {
        super(props)
        this.state = props;
    }

    render() {
        return (
            <div className="documentation-preparation-screen">
                <DocumentationPreparation {...this.state}/>
            </div>
        )
    }

    componentDidMount() {
        this._connect()
    }

    componentWillUnmount() {
        this._disconnect()
    }

    _connect() {
        this.ws = new WebSocket(socketUrl("_doc-update/" + this.props.docId))

        this.ws.onopen = () => {
            console.log('@@ open')
        }

        this.ws.onclose = () => {
            console.log('@@ close')
        }

        this.ws.onmessage = (message) => {
            const data = JSON.parse(message.data)
            this._update(data)
        };
    }

    _disconnect() {
        this.ws.close()
    }

    _update({message, keyValues, progress}) {
        this.setState({statusMessage: message, keyValues: keyValues || [], progressPercent: progress})
        if (progress >= 100) {
            setTimeout(() => window.location.reload(), 100)
        }
    }
}
