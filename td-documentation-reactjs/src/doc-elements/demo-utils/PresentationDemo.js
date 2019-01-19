import React from 'react'

import {elementsLibrary, presentationElementHandlers} from '../DefaultElementsLibrary'
import PresentationRegistry from '../presentation/PresentationRegistry'
import Presentation from '../presentation/Presentation'

const docMeta = {id: "mdoc", title: "MDoc", type: "User Guide"}

export function createPresentationDemo(content) {
    const presentationRegistry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, content)
    return () => <Presentation docMeta={docMeta} presentationRegistry={presentationRegistry}/>
}
