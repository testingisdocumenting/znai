import React from 'react'

import DocElement from './DocElement'
import elementsLibrary from './DefaultElementsLibrary'
import TestData from './TestData'

const VisualManualTest = () => (
      <div className="container">
        <div className="row">
          <DocElement content={TestData.simplePage} elementsLibrary={elementsLibrary}/>
        </div>
      </div>
    )

export default VisualManualTest
