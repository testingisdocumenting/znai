/*
 * Copyright 2020 znai maintainers
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

import AnnotatedImage from './AnnotatedImage'
import Annotations from './annotations/Annotations'
import './EmbeddedAnnotatedImage.css'

class EmbeddedAnnotatedImage extends Component {
    render() {
        const {imageSrc, shapes, align} = this.props
        const annotations = new Annotations(shapes)

        const fullClassName = "embedded-annotated-image" + (align ? " content-block " + align : "")
        return (
            <div className={fullClassName}>
                <AnnotatedImage imageSrc={imageSrc} annotations={annotations} {...this.props} isStatic="true"/>
            </div>
        )
    }
}

export default EmbeddedAnnotatedImage

