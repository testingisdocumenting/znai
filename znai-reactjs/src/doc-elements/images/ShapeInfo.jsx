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

import React from 'react'
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

class ShapeInfo extends React.Component {
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
