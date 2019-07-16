/*
 doc elements may receive additional information as meta prop that is provided via
 :include-meta: {key: 'value'}
 */

function isAllAtOnce(meta) {
    return meta && meta.allAtOnce
}

function elementMetaValue(element, key) {
    return element && element.meta ? element.meta[key] : null
}

export {isAllAtOnce, elementMetaValue}