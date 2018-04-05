import React from 'react'

import './TwoSidesLayout.css'

export function TwoSidesLayout({children}) {
    return (
        <div className="page-two-sides-layout">
            {children}
        </div>
    )
}

export function LeftPart({children}) {
    return (
        <div className="page-two-side-left-part">
            {children}
        </div>
    )
}

export function RightPart({children}) {
    return (
        <div className="page-two-side-right-part">
            {children}
        </div>
    )
}
