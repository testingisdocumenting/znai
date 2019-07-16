import * as React from 'react';

import { isLocalUrl, onLocalUrlClick } from '../../structure/links';
import { DocElementProps } from '../DocElementProps';

export interface Props extends DocElementProps {
    url: string;
}

export function Link({url, ...props}: Props) {
    const isLocal = isLocalUrl(url);
    const onClick = isLocal ? (e: React.MouseEvent<HTMLAnchorElement>) => onLocalUrlClick(e, url) : undefined;
    const targetProp = isLocal ? {} : {target: '_blank'};

    return (
        <a
            href={url}
            onClick={onClick}
            {...targetProp}
        >
            <props.elementsLibrary.DocElement {...props}/>
        </a>
    );
}
