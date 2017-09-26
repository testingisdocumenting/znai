import React from 'react'
import JSONTree from 'react-json-tree'

const jsonTheme = buildJsonTheme()
const Json = ({json}) => {
    return (
        <div className="data json">
            <JSONTree data={json}
                      theme={jsonTheme}/>
        </div>
    )
}

const Text = ({text}) => {
    return (
        <div className="data text">
            {text}
        </div>
    )
}

const PayloadData = ({type, data}) => {
    return type.indexOf('json') !== -1 ?
        <Json json={JSON.parse(data)}/> :
        <Text text={data}/>
}

const Payload = ({caption, type, data}) => {
    if (! data) {
        return null
    }

    return (
        <div className="payload">
            <div className="caption">{caption}</div>
            <PayloadData type={type} data={data}/>
        </div>
    )
}

function buildJsonTheme() {
    return {
        scheme: 'default',
        author: 'chris kempson (http://chriskempson.com)',
        base00: '#181818',
        base01: '#282828',
        base02: '#383838',
        base03: '#585858',
        base04: '#b8b8b8',
        base05: '#d8d8d8',
        base06: '#e8e8e8',
        base07: '#f8f8f8',
        base08: '#ab4642',
        base09: '#dc9656',
        base0A: '#f7ca88',
        base0B: '#a1b56c',
        base0C: '#86c1b9',
        base0D: '#7cafc2',
        base0E: '#ba8baf',
        base0F: '#a16946'
    }
}

export default Payload
