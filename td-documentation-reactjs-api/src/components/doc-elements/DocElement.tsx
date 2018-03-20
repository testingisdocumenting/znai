import * as React from 'react';

import { DocElementProps } from './DocElementProps';

/**
 * uses given set of components to render DocElements like links, paragraphs, code blocks, etc
 *
 * @param content content to render
 * @param elementsLibrary library of elements to use to render
 */
export function DocElement({content, elementsLibrary}: DocElementProps) {
    return (!content ? null : content.map((item, idx) => {
        const ElementToUse = elementsLibrary[item.type];
        if (!ElementToUse) {
            console.warn('can\'t find component to display', item.type);
            return null;
        } else {
            return (
                <ElementToUse
                    key={idx}
                    elementsLibrary={elementsLibrary}
                    {...item}
                />);
        }
    }));
}
