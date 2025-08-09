interface MatchPosition {
  start: number;
  end: number;
}

export interface PrefixSuffix {
  prefix: string;
  suffix: string;
}

function encodeTextFragment(text: string): string {
  return text.replace(/\n/g, "%0A").replace(/ /g, "%20").replace(/-/g, "%2D").replace(/,/g, "%2C");
}

export function buildHighlightAnchor(match: string, prefixSuffix: PrefixSuffix): string {
  let parts = [];
  if (prefixSuffix.prefix.length > 0) {
    parts.push(encodeTextFragment(prefixSuffix.prefix) + "-");
  }
  parts.push(match);
  if (prefixSuffix.suffix.length > 0) {
    parts.push("-" + encodeTextFragment(prefixSuffix.suffix));
  }
  return `#:~:text=${parts.join(",")}`;
}

function findAllMatches(text: string, match: string): MatchPosition[] {
  const positions: MatchPosition[] = [];
  let index = text.indexOf(match);

  while (index !== -1) {
    positions.push({ start: index, end: index + match.length });
    index = text.indexOf(match, index + 1);
  }

  return positions;
}

function findPreviousWordBoundary(text: string, position: number): number {
  // Search backwards from position for word boundary
  // Start from before the current position
  let i = position - 1;

  // Skip any whitespace/punctuation we're currently in
  while (i >= 0 && !/\w/.test(text[i])) {
    i--;
  }

  // Skip the current word
  while (i >= 0 && /\w/.test(text[i])) {
    i--;
  }

  // Now we're at the end of the previous non-word section
  // Move to the start of the next word (or start of text)
  return i + 1;
}

function findNextWordBoundary(text: string, position: number): number {
  // Search forward from position for word boundary
  let i = position;

  // Skip any whitespace/punctuation we're currently in
  while (i < text.length && !/\w/.test(text[i])) {
    i++;
  }

  // Skip the current word
  while (i < text.length && /\w/.test(text[i])) {
    i++;
  }

  // Now we're at the start of the next non-word section
  // This is our boundary
  return i;
}

function expandToWordBoundaries(
  text: string,
  start: number,
  end: number,
  wordCount: number
): { newStart: number; newEnd: number } {
  let newStart = start;
  let newEnd = end;

  // Expand backwards word by word
  for (let i = 0; i < wordCount; i++) {
    const prevBoundary = findPreviousWordBoundary(text, newStart);
    if (prevBoundary === newStart) {
      break; // No more words to expand
    }
    newStart = prevBoundary;
  }

  // Expand forwards word by word
  for (let i = 0; i < wordCount; i++) {
    const nextBoundary = findNextWordBoundary(text, newEnd);
    if (nextBoundary === newEnd) {
      break; // No more words to expand
    }
    newEnd = nextBoundary;
  }

  return { newStart, newEnd };
}

function extractPrefixSuffix(
  text: string,
  matchPos: MatchPosition,
  expandedStart: number,
  expandedEnd: number
): PrefixSuffix {
  const prefix = text.substring(expandedStart, matchPos.start);
  const suffix = text.substring(matchPos.end, expandedEnd);

  return {
    prefix: prefix,
    suffix: suffix,
  };
}

function findPrefixSuffixInText(text: string, prefix: string, match: string, suffix: string): number {
  // Search for exact pattern: prefix + match + suffix
  const fullPattern = prefix + match + suffix;
  let count = 0;
  let searchIndex = 0;

  while (searchIndex < text.length) {
    const index = text.indexOf(fullPattern, searchIndex);
    if (index === -1) break;
    count++;
    searchIndex = index + 1;
  }

  return count;
}

export function buildMatchPrefixAndSuffix(allText: string, match: string): PrefixSuffix {
  const positions = findAllMatches(allText, match);

  if (positions.length === 0) {
    return { prefix: "", suffix: "" };
  }

  if (positions.length === 1) {
    return { prefix: "", suffix: "" };
  }

  const firstMatch = positions[0];
  const maxExpansion = 10;

  for (let wordCount = 1; wordCount <= maxExpansion; wordCount++) {
    const { newStart, newEnd } = expandToWordBoundaries(allText, firstMatch.start, firstMatch.end, wordCount);
    const { prefix, suffix } = extractPrefixSuffix(allText, firstMatch, newStart, newEnd);

    const matchCount = findPrefixSuffixInText(allText, prefix, match, suffix);

    if (matchCount === 1) {
      return { prefix, suffix };
    }
  }

  const { newStart, newEnd } = expandToWordBoundaries(allText, firstMatch.start, firstMatch.end, maxExpansion);
  return extractPrefixSuffix(allText, firstMatch, newStart, newEnd);
}
