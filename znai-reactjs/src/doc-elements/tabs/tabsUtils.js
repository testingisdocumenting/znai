export function contentTabNames(content) {
    const result = []

    collectTabNamesRecursively(result, content)

    return result
}

function collectTabNamesRecursively(result, content) {
    if (!content || content.length === 0) {
        return
    }

    content.forEach(e => {
        if (e.type === 'Tabs') {
            addMissingTabNames(result, e)
        } else {
            collectTabNamesRecursively(result, e.content)
        }
    })
}

function addMissingTabNames(result, tabsDocEl) {
    tabsDocEl.tabsContent.forEach(tc => addMissing(result, tc.name))
}

function addMissing(result, name) {
    const idx = result.indexOf(name)
    if (idx !== -1) {
        return
    }

    result.push(name)
}