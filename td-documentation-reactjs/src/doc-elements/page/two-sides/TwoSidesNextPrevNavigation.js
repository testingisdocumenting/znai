import React from 'react'
import {TwoSidesLayout, TwoSidesLayoutLeftPart, TwoSidesLayoutRightPart} from './TwoSidesLayout'
import {DefaultNextPageButton, DefaultPrevPageButton} from '../default/PageDefaultNextPrevNavigation'

import './TwoSidesNextPrevNavigation.css'

const TwoSidesNextPrevNavigation = ({prevPageTocItem, nextPageTocItem, onNextPage, onPrevPage}) => {
    return (
        <TwoSidesLayout>
            <TwoSidesLayoutLeftPart>
                <div className="two-sides-prev-navigation-button">
                    <DefaultPrevPageButton tocItem={prevPageTocItem} onClick={onPrevPage}/>
                </div>
            </TwoSidesLayoutLeftPart>

            <TwoSidesLayoutRightPart>
                <div className="two-sides-next-navigation-button">
                    <DefaultNextPageButton tocItem={nextPageTocItem} onClick={onNextPage}/>
                </div>
            </TwoSidesLayoutRightPart>
        </TwoSidesLayout>
    )
}

export default TwoSidesNextPrevNavigation
