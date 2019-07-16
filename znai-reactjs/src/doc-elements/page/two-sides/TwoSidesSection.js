import React from 'react'

import {splitContentInTwoPartsSubsections} from './twoSidesContentSplitter'
import {TwoSidesLayout, TwoSidesLayoutLeftPart, TwoSidesLayoutRightPart} from './TwoSidesLayout'

import './TwoSidesSection.css'

const TwoSidesSection = ({elementsLibrary, id, title, content = []}) => {
    const subsections = splitContentInTwoPartsSubsections(content)

    return (
        <div className="two-sides-section">
            <TwoSidesLayout>
                <TwoSidesLayoutLeftPart>
                    <elementsLibrary.SectionTitle id={id} title={title}/>
                </TwoSidesLayoutLeftPart>

                <TwoSidesLayoutRightPart/>

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
            <TwoSidesLayoutLeftPart>
                <elementsLibrary.DocElement content={subsection.left} elementsLibrary={elementsLibrary}/>
            </TwoSidesLayoutLeftPart>
            <TwoSidesLayoutRightPart>
                <elementsLibrary.DocElement content={subsection.right} elementsLibrary={elementsLibrary}/>
            </TwoSidesLayoutRightPart>
        </React.Fragment>
    )
}

export default TwoSidesSection