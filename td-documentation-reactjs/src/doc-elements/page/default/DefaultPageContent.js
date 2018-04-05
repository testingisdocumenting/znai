import React from 'react'
import PageTitle from '../PageTitle'

const DefaultPageContent = (props) => {
    return (
        <React.Fragment>
            <div className="content-block">
                <PageTitle {...props}/>
            </div>
            <props.elementsLibrary.DocElement elementsLibrary={props.elementsLibrary}
                                              content={props.content}/>
        </React.Fragment>
    )
}

export default DefaultPageContent