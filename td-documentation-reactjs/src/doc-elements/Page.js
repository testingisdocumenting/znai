import * as React from "react"
import DocElement from "./DocElement"
import NavBar from './NavBar'
import elementsLibrary from './DefaultElementsLibrary'

const Page = ({title, content, renderContext, docMeta}) => {
    return (
        <div className="page">
            <NavBar renderContext={renderContext} docMeta={docMeta} />
            <div className="container">
                <div className="page-title">{title}</div>
                <DocElement content={content} elementsLibrary={elementsLibrary}/>
            </div>
        </div>)
}

export default Page
