import React, {Component} from 'react'

class Server {
    constructor(handlers) {
        this.handlers = handlers
    }

    connect() {
        this.ws = new WebSocket(socketUrl("preview"))

        this.ws.onopen = () => {
            console.log("onopen")
            this.handlers.onOpen()
        }

        this.ws.onclose = () => {
            console.log("onclose")
            this.handlers.onClose()
        }

        this.ws.onmessage = (message) => {
            console.log("message", message)

            const data = JSON.parse(message.data)

            if (data.type === 'pageUpdate') {
                this.handlers.onPageUpdate(data.pageProps)
            }

            if (data.type === 'tocUpdate') {
                this.handlers.onTocUpdate(data.toc)
            }

            if (data.type === 'multiplePagesUpdate') {
                this.handlers.onMultiplePagesUpdate(data.listOfPageProps)
            }
        };

        function socketUrl(relativeUrl) {
            let currentLocation = document.location.toString()
            const hashIdx = currentLocation.lastIndexOf("#")
            if (hashIdx !== -1) {
                currentLocation = currentLocation.substr(0, hashIdx)
            }

            const isSecure = currentLocation.indexOf("https://") !== -1
            const protocol = isSecure ? "wss" : "ws"

            return protocol + "://" + location.hostname + ":" + 8080 + "/" + relativeUrl
            // return protocol + "://" + location.hostname + ":" + location.port + "/" + relativeUrl
        }
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
        return active ? (<div className='glyphicon glyphicon-eye-open preview-icon'></div>) : null
    }
}

export default Preview