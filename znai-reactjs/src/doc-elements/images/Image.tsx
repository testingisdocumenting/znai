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

import React from "react";

import { AnnotatedImage } from "./AnnotatedImage";
import { WithElementsLibrary } from "../default-elements/DocElement";
import { elementsLibrary } from "../DefaultElementsLibrary";

interface MarkdownImageProps extends WithElementsLibrary {
  destination: string;
  inlined: boolean;
  timestamp?: number;
  title?: string;
  width?: number;
  height?: number;
  fit?: boolean;
}

const Image = ({ destination, title, inlined, fit, width = 0, height = 0, timestamp }: MarkdownImageProps) => {
  return (
    <AnnotatedImage
      imageSrc={destination}
      title={title}
      inlined={inlined}
      fit={fit}
      shapes={[]}
      width={width}
      height={height}
      timestamp={timestamp}
      elementsLibrary={elementsLibrary}
    />
  );
};

export default Image;
