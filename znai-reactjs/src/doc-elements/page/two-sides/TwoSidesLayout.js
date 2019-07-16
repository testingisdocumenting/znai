import React from 'react'

import './TwoSidesLayout.css'

export function TwoSidesLayout({className, children}) {
    const fullClassName = 'page-two-sides-layout' + (className ? ' ' + className : '')
    return (
        <div className={fullClassName}>
            {children}
        </div>
    )
}

export function TwoSidesLayoutLeftPart({children}) {
    return (
        <div className="page-two-sides-left-part">
            {children}
        </div>
    )
}

export function TwoSidesLayoutRightPart({children}) {
    return (
        <div className="page-two-sides-right-part">
            {children}
        </div>
    )
}
