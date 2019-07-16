function convertToList(v) {
    if (v === null || typeof v === 'undefined') {
        return [];
    }

    if (Array.isArray(v)) {
        return v;
    }

    return [v];
}

export {convertToList};