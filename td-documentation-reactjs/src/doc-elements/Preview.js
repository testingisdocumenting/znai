import React, {Component} from 'react'

class Server {
    constructor({onOpen, onClose, onPageUpdate}) {
        this.onOpen = onOpen
        this.onClose = onClose
        this.onPageUpdate = onPageUpdate
    }

    connect() {
        this.ws = new WebSocket(socketUrl("preview"))

        this.ws.onopen = () => {
            console.log("onopen")
            this.onOpen()
        }

        this.ws.onclose = () => {
            console.log("onclose")
            this.onClose()
        }

        this.ws.onmessage = (message) => {
            console.log("message", message)

            const data = JSON.parse(message.data)
            if (data.type === 'pageUpdate') {
                this.onPageUpdate(data.pageProps)
            }

            // var command = JSON.parse(message.data);
            // console.log("message received", command);
            //
            // if (command.type === 'reload') {
            //     console.log("reloading current page");
            //     location.reload();
            // } else if (command.type === 'open') {
            //     handleOpen(command.paths)
            // } else if (command.type === 'error') {
            //     previewUi.showError(command.message);
            // }
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
            onPageUpdate: this.props.onPageUpdate
        })

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