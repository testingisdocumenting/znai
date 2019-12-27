/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, {Component} from 'react'
import {socketUrl} from '../../utils/socket'

class Server {
    constructor(handlers) {
        this.handlers = handlers
    }

    connect() {
        this.ws = new WebSocket(socketUrl("preview"))

        this.ws.onopen = () => {
            this.handlers.onOpen()
        }

        this.ws.onclose = () => {
            this.handlers.onClose()
        }

        this.ws.onmessage = (message) => {
            const data = JSON.parse(message.data)

            if (data.type === 'pageUpdate') {
                this.handlers.onPageUpdate(data.pageProps)
            }

            if (data.type === 'tocUpdate') {
                this.handlers.onTocUpdate(data.toc)
            }

            if (data.type === 'docMetaUpdate') {
                this.handlers.onDocMetaUpdate(data.docMeta)
            }

            if (data.type === 'multiplePagesUpdate') {
                this.handlers.onMultiplePagesUpdate(data.listOfPageProps)
            }

            if (data.type === 'docReferencesUpdate') {
                this.handlers.onDocReferencesUpdate(data.docReferences)
            }

            if (data.type === 'error') {
                this.handlers.onError(data.error)
            }
        };
    }
}

class Preview extends Component {
    constructor(props) {
        super(props)
        this.state = {active: false}
    }

    componentDidMount() {
        this.server = new Server({
            onOpen: this.onConnectionOpen.bind(this),
            onClose: this.onConnectionClose.bind(this),
            ...this.props})

        this.server.connect()
    }

    componentWillUnmount() {
        // disconnect?
    }

    onConnectionOpen() {
        this.setState({active: true})
    }

    onConnectionClose() {
        this.setState({active: false})
    }

    render() {
        const {active} = this.state
        return active ? (<div className='glyphicon glyphicon-eye-open preview-icon'/>) : null
    }
}

export default Preview