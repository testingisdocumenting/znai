import React, {Component} from 'react'

const LabeledField = ({name, value}) => {
    return (<div className="labeled-field">
        <div className="field-name">{name}</div>
        <div className="field-value">{value}</div>
    </div>)
}

class ShapeInfo extends Component {
    render() {
        const {shape, isActive, onSelect} = this.props
        const className = "shape-info" + (isActive ? " selected" : "")

        return <div className={className} onMouseDown={() => onSelect(shape)}>
            { Object.keys(shape).map((key) => {
                const value = shape[key]
                return <LabeledField key={key} name={key} value={value}/>
            })}
            <LabeledField/>
        </div>
    }
}

export default ShapeInfo
