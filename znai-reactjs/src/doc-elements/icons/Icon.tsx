/*
 * Copyright 2020 znai maintainers
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

import React from 'react'
import type { FeatherAttributes } from 'feather-icons';
import feather from 'feather-icons'

import './Icon.css'

const idMapping = generateMapping()

interface Props {
  id: string;
  className?: string;
  stroke?: string;
  fill?: string;

  onClick?(): void;
}

export function Icon({id, className, stroke, fill, onClick}: Props) {
  const fullClassName = 'znai-icon ' + id + (className ? ' ' + className : '')
  const idToUse = idMapping[id] || id

  const featherIcon = feather.icons[idToUse]
  return featherIcon ? (
    <span className={fullClassName}
          onClick={onClick}
          dangerouslySetInnerHTML={{__html: featherIcon.toSvg(buildStyle(stroke, fill))}}/>
  ) : (
    <span>Icon not found: {idToUse}</span>
  )
}

function buildStyle(stroke?: string, fill?: string): FeatherAttributes {
  return {
    stroke: stroke ? `var(--znai-color-${stroke})` : 'currentColor',
    fill: fill ? `var(--znai-color-${fill})` : 'none'
  }
}

function generateMapping(): {[id: string]: string;} {
  return {
    time: 'clock',
    'info-sign': 'info',
    cog: 'settings'
  }
}
