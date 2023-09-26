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

import React, { useRef, useState } from "react";
import "./Iframe.css";

interface Props {
  src: string;
  title: string;
  aspectRatio?: string;
  fit?: boolean;
  height?: number;
}

export function Iframe(props: Props) {
  if (props.fit) {
    return <IframeFit {...props} />;
  } else {
    return <IframeVideo {...props} />;
  }
}

export function IframeFit({ src, title, height }: Props) {
  const [extracClassName, setExtraClassName] = useState("");
  const [calculatedIframeHeight, setCalculatedIframeHeight] = useState(14);
  const ref = useRef<HTMLIFrameElement>(null);
  const fullClassName = "znai-iframe fit " + extracClassName;
  const activeElement = document.activeElement?.tagName === "IFRAME" ? null : document.activeElement;
  return (
    <div className="content-block">
      <iframe
        title={title}
        src={src}
        style={{ height: height ? height : calculatedIframeHeight }}
        width="100%"
        className={fullClassName}
        ref={ref}
        onLoad={handleSize}
      />
    </div>
  );

  function handleSize() {
    setTimeout(() => {
      const htmlEl = ref!.current!.contentWindow!.document.getElementsByTagName("html")[0];
      const height = htmlEl.offsetHeight + 1;
      setCalculatedIframeHeight(height);
      setExtraClassName("visible");
      if (activeElement != null) {
        (activeElement as HTMLInputElement).focus();
      } else {
        window.focus();
      }
    }, 0);
  }
}

export function IframeVideo({ src, title, aspectRatio = "16:9" }: Props) {
  return (
    <div className="content-block">
      <div style={{ position: "relative", paddingTop: calcAspectRatioPaddingTop(aspectRatio), height: 0 }}>
        <iframe
          title={title}
          src={src}
          allowFullScreen={true}
          style={{ position: "absolute", top: 0, left: 0, width: "100%", height: "100%" }}
        />
      </div>
    </div>
  );
}

export function calcAspectRatioPaddingTop(aspectRatio: string): string {
  const [width, height] = aspectRatio.split(":");
  if (!width || !height) {
    return "56.25%";
  }

  return ((Number(height) / Number(width)) * 100.0).toFixed(2) + "%";
}
