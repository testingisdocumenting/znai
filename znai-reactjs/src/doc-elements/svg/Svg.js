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
import {isAllAtOnce} from '../meta/meta'
import {EmbeddedSvg} from './EmbeddedSvg'

import './Svg.css'

class Svg extends Component {
    constructor(props) {
        super(props)
        this.state = {loadedSvg: null}
    }

    render() {
        const {svg, svgSrc} = this.props
        const {loadedSvg, error} = this.state

        if (svg) {
            return <EmbeddedSvg {...this.props}/>
        }

        if (error) {
            return (
                <div className="znai-svg-load-error content-block">
                    Cannot load SVG: <a target="_blank" href={svgSrc}>{svgSrc}</a>
                </div>
            )
        }

        if (loadedSvg) {
            return <EmbeddedSvg {...this.props} svg={loadedSvg}/>
        }

        return null
    }

    componentDidMount() {
        this.loadSvg()
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.svgSrc !== this.props.svgSrc) {
            this.loadSvg()
        }
    }

    loadSvg() {
        const {svg, svgSrc} = this.props
        if (svg) {
            this.setState({loadedSvg: svg, error: null})
            return
        }

        fetch(svgSrc, {cache: 'force-cache'})
            .then(response => {
                if (!response.ok) {
                    throw Error(response.statusText)
                }

                return response.text()
            }).then(svgFromResponse => {
                this.setState({loadedSvg: svgFromResponse, error: null})
            })
            .catch(error => {
                console.error(error)
                this.setState({error: error.message})
            })
    }
}

const presentationSvgHandler = {
    component: Svg,
    numberOfSlides: ({idsToReveal, meta}) => {
        if (isAllAtOnce(meta)) {
            return 1
        }

        return idsToReveal ? idsToReveal.length : 1
    }
}

export {Svg, presentationSvgHandler}
