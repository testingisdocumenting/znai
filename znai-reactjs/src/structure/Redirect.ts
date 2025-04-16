import { useEffect } from "react";
import { documentationNavigation } from "./DocumentationNavigation";

interface Props {
  url: string;
}

export function Redirect({ url }: Props) {
  useEffect(() => {
    documentationNavigation.replaceUrl(documentationNavigation.fullPageUrl(url));
  }, [url]);

  return null;
}
