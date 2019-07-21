/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
