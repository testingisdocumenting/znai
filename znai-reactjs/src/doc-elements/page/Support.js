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
import {getSupportLinkPromise} from '../docMeta'

class Support extends Component {
    constructor(props) {
        super(props);

        this.state = {link: null}
    }

    componentDidMount() {
        getSupportLinkPromise().then(link => this.setState({link: link}));
    }

    render() {
        return (
            <div className="page-support">
                {this.state.link ?
                    <a href={this.state.link} target="_blank">Support</a> : null
                }
            </div>
        )
    }
}

export default Support
