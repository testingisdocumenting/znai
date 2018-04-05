import React from 'react'

import {splitContentInTwoPartsSubsections} from './twoSidesContentSplitter'
import {TwoSidesLayout, LeftPart, RightPart} from './TwoSidesLayout'

import './TwoSidesSection.css'

const TwoSidesSection = ({elementsLibrary, id, title, content}) => {
    const subsections = splitContentInTwoPartsSubsections(content)

    return (
        <div className="two-sides-section">
            <TwoSidesLayout>
                <LeftPart>
                    <elementsLibrary.SectionTitle id={id} title={title}/>
                </LeftPart>

                <RightPart/>

                {subsections.map((subsection, idx) => <Subsection key={idx}
                                                                  elementsLibrary={elementsLibrary}
                                                                  subsection={subsection}/>)}
            </TwoSidesLayout>
        </div>
    )
}

function Subsection({elementsLibrary, subsection}) {
    return (
        <React.Fragment>
            <LeftPart>
                <elementsLibrary.DocElement content={subsection.left} elementsLibrary={elementsLibrary}/>
            </LeftPart>
            <RightPart>
                <elementsLibrary.DocElement content={subsection.right} elementsLibrary={elementsLibrary}/>
            </RightPart>
        </React.Fragment>
    )
}

export default TwoSidesSection