export function findPrefixSuffixAndMatch(container) {
  const selection = window.getSelection();
  const selectedText = selection.toString().trim();

  const range = selection.getRangeAt(0);

  // Get the actual selected range's context by expanding it
  function getExpandedContext(charsBefore, charsAfter) {
    const expandedRange = range.cloneRange();

    // Expand backwards
    try {
      let moved = 0;
      let currentContainer = expandedRange.startContainer;
      let currentOffset = expandedRange.startOffset;

      while (moved < charsBefore) {
        if (currentContainer.nodeType === Node.TEXT_NODE) {
          const availableChars = currentOffset;
          const toMove = Math.min(charsBefore - moved, availableChars);
          expandedRange.setStart(currentContainer, currentOffset - toMove);
          moved += toMove;
          if (moved >= charsBefore) break;

          // Need to move to previous text node
          const prev = getPreviousTextNode(currentContainer, container);
          if (!prev) break;
          currentContainer = prev;
          currentOffset = prev.textContent.length;
        } else {
          break;
        }
      }
    } catch (e) {
      console.log("Error expanding backwards:", e);
    }

    // Expand forwards
    try {
      let moved = 0;
      let currentContainer = expandedRange.endContainer;
      let currentOffset = expandedRange.endOffset;

      while (moved < charsAfter) {
        if (currentContainer.nodeType === Node.TEXT_NODE) {
          const availableChars = currentContainer.textContent.length - currentOffset;
          const toMove = Math.min(charsAfter - moved, availableChars);
          expandedRange.setEnd(currentContainer, currentOffset + toMove);
          moved += toMove;
          if (moved >= charsAfter) break;

          // Need to move to next text node
          const next = getNextTextNode(currentContainer, container);
          if (!next) break;
          currentContainer = next;
          currentOffset = 0;
        } else {
          break;
        }
      }
    } catch (e) {
      console.log("Error expanding forwards:", e);
    }

    return expandedRange.toString();
  }

  function getPreviousTextNode(node, root) {
    const walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT, null, false);

    let current;
    let previous = null;
    while ((current = walker.nextNode())) {
      if (current === node) {
        return previous;
      }
      previous = current;
    }
    return null;
  }

  function getNextTextNode(node, root) {
    const walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT, null, false);

    let current;
    let found = false;
    while ((current = walker.nextNode())) {
      if (found) {
        return current;
      }
      if (current === node) {
        found = true;
      }
    }
    return null;
  }

  // Get a large context around the selection to identify it uniquely
  const largeContext = getExpandedContext(100, 100);
  const selectionStartInContext = largeContext.indexOf(selectedText);

  if (selectionStartInContext === -1) {
    document.getElementById("clickInfo").textContent = "Error: Could not find selection in context";
    return;
  }

  // Extract the actual prefix and suffix from the large context
  const actualPrefix = largeContext.substring(0, selectionStartInContext);
  const actualSuffix = largeContext.substring(selectionStartInContext + selectedText.length);

  console.log("Actual context around selection:");
  console.log("Prefix (last 50 chars):", actualPrefix.slice(-50));
  console.log("Selected:", selectedText);
  console.log("Suffix (first 50 chars):", actualSuffix.slice(0, 50));

  // Get the full text to test uniqueness
  const fullText = container.innerText;

  // Count total occurrences
  let totalOccurrences = 0;
  let idx = fullText.indexOf(selectedText);
  while (idx !== -1) {
    totalOccurrences++;
    idx = fullText.indexOf(selectedText, idx + 1);
  }

  if (totalOccurrences === 1) {
    // Even if unique, we want min 10 chars for prefix and suffix
    const minimalPrefix = actualPrefix.slice(-10) || actualPrefix;
    const minimalSuffix = actualSuffix.slice(0, 10) || actualSuffix;

    return {
      text: selectedText,
      prefix: minimalPrefix,
      suffix: minimalSuffix,
    };
  }

  // Find minimal prefix/suffix from the actual context (but at least 10 chars)
  let minimalPrefix = "";
  let minimalSuffix = "";
  let foundUnique = false;

  // Try with both prefix and suffix, starting at 10 chars each
  const minLength = 10;

  outerLoop: for (let totalLen = minLength * 2; totalLen <= 100; totalLen++) {
    // Try different distributions, but ensure both are at least minLength
    for (
      let prefixLen = minLength;
      prefixLen <= totalLen - minLength && prefixLen <= actualPrefix.length;
      prefixLen++
    ) {
      const suffixLen = totalLen - prefixLen;
      if (suffixLen < minLength || suffixLen > actualSuffix.length) continue;

      const testPrefix = actualPrefix.slice(-prefixLen);
      const testSuffix = actualSuffix.slice(0, suffixLen);
      const pattern = testPrefix + selectedText + testSuffix;

      let count = 0;
      let idx = fullText.indexOf(pattern);
      while (idx !== -1) {
        count++;
        idx = fullText.indexOf(pattern, idx + 1);
      }

      if (count === 1) {
        minimalPrefix = testPrefix;
        minimalSuffix = testSuffix;
        foundUnique = true;
        console.log(
          `Found unique with combination: prefix="${minimalPrefix}" (${minimalPrefix.length}), suffix="${minimalSuffix}" (${minimalSuffix.length})`
        );
        break outerLoop;
      }
    }
  }

  // If not found with both, try larger contexts
  if (!foundUnique) {
    // Just use 10 chars from each side if available
    minimalPrefix = actualPrefix.slice(-Math.max(minLength, actualPrefix.length));
    minimalSuffix = actualSuffix.slice(0, Math.max(minLength, actualSuffix.length));
    console.log("Using default minimum context");
  }

  // Clean up to word boundaries if it doesn't go below minimum
  if (minimalPrefix.length > minLength + 5) {
    const lastSpace = minimalPrefix.lastIndexOf(" ");
    if (lastSpace > minLength) {
      const cleanPrefix = minimalPrefix.substring(lastSpace + 1);
      if (cleanPrefix.length >= minLength) {
        // Verify still unique
        const pattern = cleanPrefix + selectedText + minimalSuffix;
        if (fullText.indexOf(pattern) !== -1 && fullText.indexOf(pattern, fullText.indexOf(pattern) + 1) === -1) {
          minimalPrefix = cleanPrefix;
        }
      }
    }
  }

  return {
    text: selectedText,
    prefix: minimalPrefix,
    suffix: minimalSuffix,
  };
}
