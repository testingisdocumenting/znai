import DefaultPageContent from './DefaultPageContent'
import ApiPageContent from './api/ApiPageContent'
import TwoSidesPageContent from './two-sides/TwoSidesPageContent'

class PageTypesRegistry {
    _contentComponentByType = {}

    expandToc(tocItem) {
        return this._contentComponentByType[pageType(tocItem)].expandToc
    }

    pageContentComponent(tocItem) {
        return this._contentComponentByType[pageType(tocItem)].pageContentComponent
    }

    registerContentComponent(type, {pageContentComponent, expandToc}) {
        this._contentComponentByType[type] = {pageContentComponent, expandToc}
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

pageTypesRegistry.registerContentComponent(defaultType, {pageContentComponent: DefaultPageContent, expandToc: true})
pageTypesRegistry.registerContentComponent('api', {pageContentComponent: ApiPageContent, expandToc: false})
pageTypesRegistry.registerContentComponent('two-sides', {pageContentComponent: TwoSidesPageContent, expandToc: true})

export {pageTypesRegistry}
