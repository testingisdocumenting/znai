import React from 'react'

import Table from '../table/Table';

import './LangClass.css'

function LangClass({name, description, methods, ...props}) {
    return (
        <div className="lang-class content-block">
            <div className="class-name">{name}</div>
            <div className="description-and-methods">
                <div className="class-description">{description}</div>
                {methods.map(m => <LangMethod key={methodKey(m)} {...m} {...props}/>)}
            </div>
        </div>
    )
}

function LangMethod({name, description, params, ...props}) {
    return (
        <div className="lang-method">
            <div className="method-name">
                {name}
            </div>

            <div className="description-and-params">
                <div className="method-description">{description}</div>
                <div className="method-params">
                    <TableOfParams params={params} {...props}/>
                </div>
            </div>
        </div>
    )
}

function TableOfParams({params, ...props}) {
    const columns = [{name: 'name', width: '15%', align: 'right'}, {name: 'description'}]
    const styles = ['no-header', 'middle-vertical-lines-only']
    const data = params.map(p => [
        [{type: 'InlinedCode', code: p.name}],
        p.description])

    const table = {columns, styles, data}
    return <Table table={table} {...props}/>
}

function methodKey(m) {
    return m.name + ' ' + m.params.map(p => p.name).join(',')
}

export default LangClass
