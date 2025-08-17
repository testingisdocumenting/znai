import { useEffect } from "react";
import { TextHighlighter } from "./textHighlihter";
import { mainPanelClassName } from "../../layout/classNames";
import { extractHighlightParams } from "./highlightUrl";

export function HighlightUrlText() {
  useEffect(() => {
    const params = extractHighlightParams();
    if (params) {
      const container = document.querySelector(mainPanelClassName) || document.body;
      const highlighter = new TextHighlighter(container);
      highlighter.highlight(params.selection, params.prefix, params.suffix);
      
      const firstHighlight = document.querySelector(".znai-highlight");
      if (firstHighlight) {
        firstHighlight.scrollIntoView({ behavior: "smooth", block: "center" });
      }
    }
  }, []);

  return null;
}
