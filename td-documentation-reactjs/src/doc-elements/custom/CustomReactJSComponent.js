import React from 'react'

import './CustomReactJSComponent.css'

const CustomReactJSComponent = ({namespace, name, props}) => {
    const components = window[namespace]
    if (! components) {
        return <ErrorPlaceholder>No "{namespace}" components namespace found</ErrorPlaceholder>
    }

    const CustomComponent = components[name]
    if (! CustomComponent) {
        return <ErrorPlaceholder>No "{name}" component found in "{namespace}"</ErrorPlaceholder>
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
