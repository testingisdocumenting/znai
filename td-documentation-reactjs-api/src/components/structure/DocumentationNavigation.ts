import { Promise } from 'es6-promise';
import { getDocId } from './docMeta';
import { DocCoordinate } from './DocCoordinate';

const index = {dirName: '', fileName: 'index'};

export type UrlChangeListener = (url: string) => void;

export class DocumentationNavigation {
    documentationId: string;
    listeners: UrlChangeListener[];

    constructor() {
        this.documentationId = 'NA';
        this.listeners = [];

        // server side rendering guard
        if (window.addEventListener) {
            window.addEventListener('popstate', (e) => {
                this.notifyNewUrl(document.location.pathname + (document.location.hash));
            });
        }
    }

    addUrlChangeListener(listener: UrlChangeListener) {
        this.listeners.push(listener);
    }

    removeUrlChangeListener(listener: UrlChangeListener) {
        const idx = this.listeners.indexOf(listener);
        if (idx === -1) {
            return;
        }

        this.listeners.splice(idx, 1);
    }

    buildUrl(coord: DocCoordinate) {
        return '/' + (this.documentationId ? this.documentationId + '/' : '') + (coord.dirName + '/' + coord.fileName) +
            (coord.pageSectionId ? ('#' + coord.pageSectionId) : '');
    }

    navigateToPage(coord: DocCoordinate) {
        return this.navigateToUrl(this.buildUrl(coord));
    }

    navigateToUrl(url: string) {
        history.pushState({}, '', url);
        return this.notifyNewUrl(url);
    }

    scrollToAnchor(anchorId: string) {
        const anchor = document.getElementById(anchorId);
        if (anchor) {
            anchor.scrollIntoView();
        }
    }

    notifyNewUrl(url: string) {
        const promises = this.listeners.map((l) => l(url));
        return Promise.all(promises);
    }

    currentPageLocation() {
        return window.location ?
            {
                ...this.extractPageLocation(window.location.pathname),
                anchorId: window.location.hash.length ? window.location.hash.substr(1) : ''
            } :
            '/server/side';
    }

    extractPageLocation(url: string) {
        const hashIdx = url.indexOf('#');
        const anchorId = hashIdx >= 0 ? url.substr(hashIdx + 1) : '';
        url = hashIdx >= 0 ? url.substr(0, hashIdx) : url;

        const parts = url.split('/').filter(p => p !== '..' && p.length > 0);

        // url starts with /<doc-id> ?
        if (url.substr(1) === getDocId()) {
            return index;
        }

        if (parts.length < 2) {
            return index;
        }

        // something/dir-name/file-name#id
        // /dir-name/file-name
        return {dirName: parts[parts.length - 2], fileName: parts[parts.length - 1], anchorId: anchorId};
    }
}

const documentationNavigation = new DocumentationNavigation();

export { documentationNavigation };
