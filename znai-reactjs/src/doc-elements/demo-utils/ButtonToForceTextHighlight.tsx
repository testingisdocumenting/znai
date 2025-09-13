import React from "react";

interface Props {
  textToHighlight: string;
}

export function ButtonToForceTextHighlight({ textToHighlight }: Props) {
  return (
    <button
      onClick={() => {
        const url = new URL(window.location.href);
        url.searchParams.set("highlightSelection", textToHighlight);
        url.searchParams.set("highlightPrefix", "");
        url.searchParams.set("highlightSuffix", "");
        url.searchParams.set("highlightQuestion", "sample question");
        history.pushState(null, "", url.toString());
        location.reload();
      }}
    >
      simulate highlight inside
    </button>
  );
}
