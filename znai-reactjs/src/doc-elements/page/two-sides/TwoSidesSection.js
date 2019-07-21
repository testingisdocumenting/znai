/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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