import React from 'react'
import DocumentationPreparation from './DocumentationPreparation'
import DocumentationPreparationScreen from './DocumentationPreparationScreen'

const simplePreparationState = {
    docId: 'my-documentation',
    statusMessage: 'checking new version content',
    progressPercent: 35,
    keyValues: [
        {key: 'code base id', value: 'code_base_id'},
        {key: 'branch', value: 'branch_name'},
    ]}

export function documentationPreparationDemo(registry) {
    registry
        .add('only message', <DocumentationPreparation {...simplePreparationState}/>)
        .add('screen', <DocumentationPreparationScreen {...simplePreparationState}
                                                       progressPercent={0}
                                                       statusMessage={""}
                                                       keyValues={[]}/>)
}
