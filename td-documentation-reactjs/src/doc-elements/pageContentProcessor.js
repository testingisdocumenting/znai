function process(pageContent) {
    const firstSectionIdx = findFirstSectionIdx(pageContent)
    let isSectionAbsent = firstSectionIdx === -1;

    const elementsOutsideSection = isSectionAbsent ?
        pageContent :
        pageContent.slice(0, firstSectionIdx)

    const restOfElements = isSectionAbsent ?
        [] :
        pageContent.slice(firstSectionIdx)

    return elementsOutsideSection.length ?
        [{type: "Section", title: "", id: "", content: elementsOutsideSection}].concat(restOfElements):
        pageContent
}

function findFirstSectionIdx(content) {
    for (let i = 0, len = content.length; i < len; i++) {
        if (content[i].type === 'Section') {
            return i
        }
    }

    return -1
}

export default {process}
