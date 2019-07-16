import DefaultPageContent from './default/DefaultPageContent'
import PageDefaultBottomPadding from './default/PageDefaultBottomPadding'
import {PageDefaultNextPrevNavigation} from './default/PageDefaultNextPrevNavigation'
import ApiPageContent from './api/ApiPageContent'
import TwoSidesPageContent from './two-sides/TwoSidesPageContent'
import TwoSidesNextPrevNavigation from './two-sides/TwoSidesNextPrevNavigation'
import TwoSidesPageBottomPadding from './two-sides/TwoSidesPageBottomPadding'

class PageTypesRegistry {
    _contentComponentByType = {}

    expandToc(tocItem) {
        return this._registered(tocItem).expandToc
    }

    pageContentComponent(tocItem) {
        return this._registered(tocItem).pageContentComponent
    }

    pageBottomPaddingComponent(tocItem) {
        return this._registered(tocItem).pageBottomPaddingComponent
    }

    nextPrevNavigationComponent(tocItem) {
        return this._registered(tocItem).nextPrevNavigationComponent
    }

    registerContentComponent(type, settings) {
        this._contentComponentByType[type] = settings
    }

    _registered(tocItem) {
        return this._contentComponentByType[pageType(tocItem)]
    }
}

const defaultType = 'default'
function pageType(tocItem) {
    if (!tocItem.pageMeta || !tocItem.pageMeta.type) {
        return defaultType
    }

    return tocItem.pageMeta.type[0]
}

const pageTypesRegistry = new PageTypesRegistry()

pageTypesRegistry.registerContentComponent(defaultType, {
    pageContentComponent: DefaultPageContent,
    nextPrevNavigationComponent: PageDefaultNextPrevNavigation,
    pageBottomPaddingComponent: PageDefaultBottomPadding,
    expandToc: true
})

pageTypesRegistry.registerContentComponent('api', {
    pageContentComponent: ApiPageContent,
    nextPrevNavigationComponent: PageDefaultNextPrevNavigation,
    pageBottomPaddingComponent: PageDefaultBottomPadding,
    expandToc: false
})

pageTypesRegistry.registerContentComponent('two-sides', {
    pageContentComponent: TwoSidesPageContent,
    nextPrevNavigationComponent: TwoSidesNextPrevNavigation,
    pageBottomPaddingComponent: TwoSidesPageBottomPadding,
    expandToc: true
})

export {pageTypesRegistry}
