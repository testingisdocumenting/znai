import React from 'react'

import ApiSingleRowParam from '../../api/ApiSingleRowParam'

import './SchemaAsTable.css'

export class SchemaAsTable extends React.Component {
    constructor(props) {
        super(props)

        this.path = 'root'

        this.state = {
            expandedPaths: this._initialExpandedPaths()
        }

        this.structure = {onToggle: this.onToggle, isExpanded: this.isExpanded}
    }

    render() {
        return (
            <div className="open-api-schema-as-table">
                {this.renderTopLevel()}
            </div>
        )
    }

    renderTopLevel() {
        const {schema, elementsLibrary} = this.props

        if (isObject(schema)) {
            return <ParamsTable params={objectToArray(schema.properties)}
                                path={this.path}
                                structure={this.structure}
                                elementsLibrary={elementsLibrary}/>
        }

        return (
            <Param name="root"
                   path={this.path}
                   item={schema}
                   structure={this.structure}
                   elementsLibrary={elementsLibrary}/>
        )
    }

    isExpanded = (path) => this.state.expandedPaths.hasOwnProperty(path)

    onToggle = (path) => {
        if (this.isExpanded(path)) {
            this.onCollapse(path)
        } else {
            this.onExpand(path)
        }
    }

    onExpand = (path) => {
        this.setState(prev => ({expandedPaths: addToObj(prev.expandedPaths, path)}))
    }

    onCollapse = (path) => {
        this.setState(prev => ({expandedPaths: removeFromObj(prev.expandedPaths, path)}))
    }

    _initialExpandedPaths() {
        return isObject(this.props.schema) ? {} : {[this.path]: true}
    }
}

function ParamsTable({params, structure, path, isNested, elementsLibrary}) {
    const className = 'open-api-schema-table' + (isNested ? ' nested' : '')

    return (
        <div className={className}>
            {params.map((p, idx) => <Param key={p.name}
                                           name={p.name}
                                           item={p.item}
                                           path={path + '.' + p.name}
                                           elementsLibrary={elementsLibrary}
                                           structure={structure}/>)}
        </div>
    )
}

function Param({name, item, path, structure, elementsLibrary}) {
    if (isObject(item)) {
        return <ObjectParam name={name}
                            type="object"
                            description={item.description}
                            params={objectToArray(item.properties)}
                            path={path}
                            elementsLibrary={elementsLibrary}
                            structure={structure}/>
    }

    if (isArrayOfSimple(item)) {
        return <SingleParam name={name}
                            type={'array of ' + item.items.type + 's'}
                            elementsLibrary={elementsLibrary}
                            description={item.description}/>
    }

    if (isArrayOfObject(item)) {
        return <ObjectParam name={name}
                            type="array of objects"
                            description={item.description}
                            params={objectToArray(item.items.properties)}
                            path={path}
                            elementsLibrary={elementsLibrary}
                            structure={structure}/>
    }

    return <SingleParam name={name}
                        type={item.type}
                        elementsLibrary={elementsLibrary}
                        description={item.description}/>
}

function ObjectParam({name, type, description, params, path, structure, elementsLibrary}) {
    const isExpanded = structure.isExpanded(path)

    const nested = isExpanded ? (
        <ParamsTable params={params}
                     path={path}
                     isNested={true}
                     elementsLibrary={elementsLibrary}
                     structure={structure}/>
    ) : null

    return (
        <React.Fragment>
            <SingleParam name={name}
                         type={type}
                         description={description}
                         path={path}
                         isExpandable={true}
                         isExpanded={isExpanded}
                         elementsLibrary={elementsLibrary}
                         structure={structure}/>
            {nested}
        </React.Fragment>
    )
}

function SingleParam({name, type, description, path, structure, elementsLibrary, isExpandable, isExpanded}) {
    const expandToggle = isExpandable &&
        <div className="expand-toggle"
             onClick={() => structure.onToggle(path)}>
            {isExpanded ? '-' : '+'}
        </div>

    return (
        <ApiSingleRowParam name={name}
                           type={type}
                           description={description}
                           isExpanded={isExpanded}
                           elementsLibrary={elementsLibrary}
                           actionElements={expandToggle}/>
    )
}

function isObject(item) {
    return item.hasOwnProperty('properties')
}

function isArray(item) {
    return item.type === 'array'
}

function isArrayOfSimple(item) {
    return isArray(item) && ! isObject(item.items)
}

function isArrayOfObject(item) {
    return isArray(item) && isObject(item.items)
}

function objectToArray(object) {
    return Object.keys(object).map(k => {
        const item = object[k]
        return {name: k, item: item}
    })
}

function addToObj(obj, key) {
    return {...obj, [key]: true}
}

function removeFromObj(ob, key) {
    const copy = {...ob}
    delete copy[key]

    return copy
}

