import React, {Component} from 'react'
import './ShapeInfo.css'

const LabeledField = ({name, value, onChange}) => {
    const Value = name === 'type' || name === 'id' ?
        value :
        <input value={value} onChange={(e) => {
            const newValue = e.target.value
            const isText = name === 'text' || name === 'color'
            onChange(name, isText ? newValue : newValue | 0)}
        }/>

    return (<div className="labeled-field">
        <div className="field-name">{name}</div>
        <div className="field-value">{Value}</div>
    </div>)
}

class ShapeInfo extends Component {
    render() {
        const {shape, isSelected, onSelect, onChange} = this.props
        const className = "shape-info" + (isSelected ? " selected" : "")

        const onShapeChange = (name, value) => {
            onChange({...shape, [name]: value})
        }

        const typeAndLabel = <div key="type-and-label">{shape.type + " " + shape.id}</div>

        const fields = <div className="fields">{Object.keys(shape).filter(key => key !== 'id' && key !== 'type').map((key) => {
                return <LabeledField key={key} name={key} value={shape[key]}
                                     onChange={onShapeChange}/>
            })}</div>

        return <div className={className} onMouseDown={() => onSelect(shape)}>
            {isSelected ? [typeAndLabel, fields] : typeAndLabel}
        </div>
    }
}

export default ShapeInfo
