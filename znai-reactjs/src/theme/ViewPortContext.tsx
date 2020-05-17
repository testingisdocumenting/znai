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

import React, {useContext, useEffect, useState} from "react";

const mobileMediaQuery = window.matchMedia("(max-width: 940px)")

interface ViewPort {
  isMobile: boolean;
}

export const ViewPortContext = React.createContext<ViewPort>({isMobile: isViewPortMobile()})

interface ProviderProps {
  onLayoutChange?(isMobile: boolean): void;
  children: React.ReactNode;
  isMobileForced?: boolean;
}

export function ViewPortProvider({isMobileForced, onLayoutChange, children}: ProviderProps) {
  const [isMobile, setIsMobile] = useState(isMobileForced === undefined ?
    isViewPortMobile() : isMobileForced);

  useEffect(() => {
    mobileMediaQuery.addListener(onMediaQueryBreakpoint);
    return () => mobileMediaQuery.removeListener(onMediaQueryBreakpoint);

    function onMediaQueryBreakpoint(e: MediaQueryListEvent) {
      const isMobile = e.matches;

      if (onLayoutChange) {
        onLayoutChange(isMobile);
      }

      setIsMobile(isMobile);
    }
  }, [onLayoutChange])

  return (
    <ViewPortContext.Provider value={{isMobile}}>
      {children}
    </ViewPortContext.Provider>
  )
}

export function isViewPortMobile() {
  return mobileMediaQuery.matches;
}

export function useIsMobile() {
  const viewPortContext = useContext(ViewPortContext);
  return viewPortContext.isMobile;
}