import React from 'react'

import elementsLibrary from './DefaultElementsLibrary'
import TestData from './TestData'

const VisualManualTest = () => (
          <elementsLibrary.Page {...TestData.simplePage}/>
    )

export default VisualManualTest
