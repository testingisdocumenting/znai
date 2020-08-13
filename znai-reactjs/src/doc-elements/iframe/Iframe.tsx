/*
 * Copyright 2020 znai maintainers
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

import React from 'react';

interface Props {
  src: string;
  title: string;
  aspectRatio?: string;
}

export function Iframe({src, title, aspectRatio = "16:9"}: Props) {
  return (
    <div className="content-block">
      <div style={{position: "relative", paddingTop: calcAspectRatioPaddingTop(aspectRatio), height: 0}}>
        <iframe title={title}
                src={src}
                frameBorder={0}
                allowFullScreen={true}
                style={{position: "absolute", top: 0, left: 0, width: "100%", height: "100%"}}>
        </iframe>
      </div>
    </div>
  );
}

export function calcAspectRatioPaddingTop(aspectRatio: string): string {
  const [width, height] = aspectRatio.split(":")
  if (!width || !height) {
    return "56.25%"
  }

  return (((Number(height) / Number(width)) * 100.0).toFixed(2))+ '%';
}