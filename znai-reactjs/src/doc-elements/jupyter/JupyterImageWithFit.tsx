/*
 * Copyright 2025 znai maintainers
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
import { AnnotatedImage } from "../images/AnnotatedImage";
import { useImageDimensions } from "./useImageDimensions";

interface Props {
  imageSrc: string;
  alt: string;
  elementsLibrary: any;
}

export function JupyterImageWithFit({ imageSrc, alt, elementsLibrary }: Props) {
  const { dimensions, handleImageLoad } = useImageDimensions();

  if (!dimensions) {
    return (
      <img src={imageSrc} alt={alt} onLoad={handleImageLoad} style={{ visibility: "hidden", position: "absolute" }} />
    );
  }

  return (
    <AnnotatedImage
      imageSrc={imageSrc}
      width={dimensions.width}
      height={dimensions.height}
      fit={true}
      shapes={[]}
      alt={alt}
      elementsLibrary={elementsLibrary}
    />
  );
}
