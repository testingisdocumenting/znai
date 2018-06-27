import React from 'react'
import Landing from './Landing'

const documentations = [
    {id: 'hipchat', name: 'HipChat', category: 'Collaboration', description: 'short description of HipChat. short description of HipChat'},
    {id: null, name: 'Slack', category: 'Collaboration', url: 'https://slack.com', description: 'external link example'},
    {id: 'mdoc', name: 'MDoc', category: 'Documentation', description: 'short description of mdoc. short description of mdoc. '},
    {id: 'python', name: 'Python', category: 'Languages', description: 'short description of python. short description of python. '},
    {id: 'java', name: 'Java', category: 'Languages', description: 'short description of java. short description of java. '},
    {id: 'javascript', name: 'JavaScript', category: 'Languages', description: 'short description of javascript. short description of javascript. '},
    {id: 'kotlin', name: 'Kotlin', category: 'Languages', description: 'short description of kotlin. short description of kotlin. short description of kotlin. short description of kotlin. '},
]

export function landingDemo(registry) {
    registry
        .add('landing', <Landing documentations={documentations} title="Company" type="Guides"/>)
}
