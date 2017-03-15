import React from 'react'

import {presentationElementHandlers} from '../DefaultElementsLibrary'

import Presentation from './Presentation'
import PresentationRegistry from './PresentationRegistry'

import testData from '../TestData'

const registry = new PresentationRegistry(presentationElementHandlers, testData.documentation.page.content)
const PresentationDemo = (props) => <Presentation presentationRegistry={registry}/>

export default PresentationDemo

