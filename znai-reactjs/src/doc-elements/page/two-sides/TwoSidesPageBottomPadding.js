import React from 'react'

import {TwoSidesLayout, TwoSidesLayoutLeftPart, TwoSidesLayoutRightPart} from './TwoSidesLayout'

import './TwoSidesPageBottomPadding.css'

export default function TwoSidesPageBottomPadding() {
    return (
        <TwoSidesLayout className="two-sides-page-bottom-padding">
            <TwoSidesLayoutLeftPart>&nbsp;</TwoSidesLayoutLeftPart>
            <TwoSidesLayoutRightPart>&nbsp;</TwoSidesLayoutRightPart>
        </TwoSidesLayout>
    )
}