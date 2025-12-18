/*
 * Copyright 2025 znai maintainers
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

import './CustomReactJSComponent.css'

const CustomReactJSComponent = ({namespace, name, props}) => {
    const components = window[namespace]
    if (! components) {
        return <ErrorPlaceholder>No &quot;{namespace}&quot; components namespace found</ErrorPlaceholder>
    }

    const CustomComponent = components[name]
    if (! CustomComponent) {
        return <ErrorPlaceholder>No &quot;{name}&quot; component found in &quot;{namespace}&quot;</ErrorPlaceholder>
    }

    return <CustomComponent {...props}/>
}

function ErrorPlaceholder({children}) {
    return (
        <div className="custom-component-error-placeholder">
            {children}
        </div>
    )
}

export default CustomReactJSComponent
