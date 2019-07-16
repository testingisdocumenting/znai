import * as React from 'react';

import { getDocId } from './docMeta';
import { documentationNavigation } from './DocumentationNavigation';

function isLocalUrl(url: string) {
    if (!window.document) {
        return false;
    }

    return url.startsWith('/' + getDocId());
}

function onLocalUrlClick(e: React.MouseEvent<HTMLAnchorElement>, url: string) {
    e.preventDefault();
    documentationNavigation.navigateToUrl(url);
}

export { isLocalUrl, onLocalUrlClick };
