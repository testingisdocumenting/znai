import DefaultPageContent from './default/DefaultPageContent'
import {PageDefaultNextPrevNavigation} from './default/PageDefaultNextPrevNavigation'
import ApiPageContent from './api/ApiPageContent'
import TwoSidesPageContent from './two-sides/TwoSidesPageContent'
import TwoSidesNextPrevNavigation from './two-sides/TwoSidesNextPrevNavigation'

class PageTypesRegistry {
    _contentComponentByType = {}

    expandToc(tocItem) {
        return this._registered(tocItem).expandToc
    }

    pageContentComponent(tocItem) {
        return this._registered(tocItem).pageContentComponent
    }

    nextPrevNavigationComponent(tocItem) {
        return this._registered(tocItem).nextPrevNavigationComponent
    }

    registerContentComponent(type, {pageContentComponent, nextPrevNavigationComponent, expandToc}) {
        this._contentComponentByType[type] = {pageContentComponent, nextPrevNavigationComponent, expandToc}
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
    expandToc: true
})

pageTypesRegistry.registerContentComponent('api', {
    pageContentComponent: ApiPageContent,
    expandToc: false,
    nextPrevNavigationComponent: PageDefaultNextPrevNavigation,
})

pageTypesRegistry.registerContentComponent('two-sides', {
    pageContentComponent: TwoSidesPageContent,
    nextPrevNavigationComponent: TwoSidesNextPrevNavigation,
    expandToc: true
})

export {pageTypesRegistry}
