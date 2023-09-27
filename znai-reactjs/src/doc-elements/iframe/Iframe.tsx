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
  light?: any;
  dark?: any;
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

let activeElement: any = null;
export function IframeFit({ src, title, height, light, dark }: Props) {
  const ref = useRef<HTMLIFrameElement>(null);
  const [extracClassName, setExtraClassName] = useState("");
  const [calculatedIframeHeight, setCalculatedIframeHeight] = useState(14);

  React.useEffect(() => {
    // TODO theme integration via context
    // @ts-ignore
    window.znaiTheme.addChangeHandler(onThemeChange);

    // @ts-ignore
    return () => window.znaiTheme.removeChangeHandler(onThemeChange);

    function onThemeChange() {
      injectCssProperties(ref, dark, light);
    }
  }, [dark, light]);

  const fullClassName = "znai-iframe fit " + extracClassName;

  if (document.activeElement?.tagName !== "IFRAME") {
    activeElement = document.activeElement;
  }

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
      const document = ref!.current!.contentWindow!.document;
      const htmlEl = document.getElementsByTagName("html")[0];
      const height = htmlEl.offsetHeight + 1;
      // @ts-ignore
      injectCssProperties(ref, dark, light);
      setCalculatedIframeHeight(height);
      setExtraClassName("visible");
      if (activeElement != null) {
        if (activeElement.tagName === "INPUT") {
          activeElement.focus();
        } else {
          window.focus();
        }
      }
    }, 0);
  }
}

function queryIsDarkTheme() {
  // @ts-ignore
  return window.znaiTheme.name === "znai-dark";
}

function injectCssProperties(iframeRef: any, dark: any, light: any) {
  const vars = queryIsDarkTheme() ? dark : light;

  if (!vars) {
    return;
  }

  const document = iframeRef!.current!.contentWindow!.document;
  const documentEl = document.documentElement;

  Object.keys(vars).forEach((k) => {
    documentEl.style.setProperty(k, vars[k]);
  });
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
