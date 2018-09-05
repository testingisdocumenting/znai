import React from 'react'

import Latex from './Latex'

export function latexDemo(registry) {
    registry
        .add('block formula', <Latex latex={"c = \\pm\\sqrt{a^2 + b^2}"}/>)
}