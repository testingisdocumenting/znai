import { MutableRefObject, useEffect, useRef, useState } from "react";
import { addHighlightedTextListener, removeHighlightedTextListener } from "./HighlightedText";
import { reapplyTextHighlights } from "./AllTextHighlights";

export function moveAndHideHtmlElementForAutoScroll(element: HTMLElement, newParent: HTMLElement) {
  const originalParent = element.parentNode;
  const originalNextSibling = element.nextSibling;
  const originalDisplay = element.style.display;
  const originalHeight = element.style.height;
  const originalOverflow = element.style.overflow;

  newParent.appendChild(element);
  element.style.display = "block";
  element.style.height = "0";
  element.style.overflow = "hidden";

  return function restore() {
    element.style.display = originalDisplay;
    element.style.height = originalHeight;
    element.style.overflow = originalOverflow;

    if (originalNextSibling) {
      originalParent!.insertBefore(element, originalNextSibling);
    } else {
      originalParent!.appendChild(element);
    }
  };
}

/*

Use can highlight text inside hidden by default blocks of text.
When a page with the highlight is opened, we want the browser to scroll
to the element that contains the hidden text.

To achieve that, components with hidden content should still
render the content but with display:none. We need the content
to be part of the DOM so the highlight engine can find them.

Components can subscribe to "highlight" event and receive the firstHighlightElement
in the callback.

If the element is part of the hidden section, components can
highlight their toggles to let users know there is a hidden, highlighted text.
But they also need to temporary move the "firstHighlightElement" somehwere in the
visible zone for the scroll to happen.

Hook below attempts to encapsulate this logic.

*/

export function useHighlightOfHiddenElement(
  containerRef: MutableRefObject<HTMLElement | null>,
  hiddenContainerRef: MutableRefObject<HTMLElement | null>,
  contentVisibilityTrigger: any
) {
  const restoreFirstHighlightElementFunRef = useRef<(() => void) | null>(null);
  const [hasHiddenHighlightedElement, setHasHiddenHighlightedElement] = useState(false);
  const onlyOnce = useRef<boolean>(false);

  useEffect(() => {
    const listener = {
      onUserDrivenTextHighlight: (firstHighlightElement: HTMLElement) => {
        if (
          containerRef.current &&
          hiddenContainerRef.current &&
          hiddenContainerRef.current.contains(firstHighlightElement) &&
          !onlyOnce.current
        ) {
          restoreFirstHighlightElementFunRef.current = moveAndHideHtmlElementForAutoScroll(
            firstHighlightElement,
            containerRef.current
          );
          setHasHiddenHighlightedElement(true);
          onlyOnce.current = true;
        }
      },
    };
    addHighlightedTextListener(listener);
    return () => {
      removeHighlightedTextListener(listener);
    };
  }, []);

  // this most likely won't work with Tabs
  useEffect(() => {
    if (restoreFirstHighlightElementFunRef.current && onlyOnce.current) {
      restoreFirstHighlightElementFunRef.current();
      restoreFirstHighlightElementFunRef.current = null;
    }
    reapplyTextHighlights();
  }, [contentVisibilityTrigger]);

  return hasHiddenHighlightedElement;
}
