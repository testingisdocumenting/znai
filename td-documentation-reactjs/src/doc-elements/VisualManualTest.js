import React from 'react'

import elementsLibrary from './DefaultElementsLibrary'
import TestData from './TestData'

import SearchPopup from './SearchPopup'

const onClose = () => console.log("on close")

const VisualManualTest = () => (
    <div>
        <elementsLibrary.Page {...TestData.simplePage} />
        <SearchPopup onClose={onClose} toc={TestData.toc}/>
    </div>
)

export default VisualManualTest
