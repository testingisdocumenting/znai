/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import * as React from "react";

export type ElementsLibraryMap = { [key: string]: any };
export type DocElementContent = DocElementPayload[];

export interface DocElementPayload {
  type: any;
  content?: DocElementContent;
}

export interface WithElementsLibrary {
  elementsLibrary: ElementsLibraryMap;
}

export interface DocElementProps extends WithElementsLibrary {
  content: DocElementContent;
  next?: DocElementPayload;
  prev?: DocElementPayload;
}

/**
 * uses given set of components to render DocElements like links, paragraphs, code blocks, etc
 *
 * @param content content to render
 * @param elementsLibrary library of elements to use to render
 */
export function DocElement({ content, elementsLibrary }: DocElementProps) {
  if (!content) {
    return null;
  }

  const contentProvider = new ElementsContentProvider(content);

  const renderedElements = [];
  while (contentProvider.peekCurrent()) {
    const found = findRenderComponent(elementsLibrary, contentProvider);
    const ElementToUse = found.component;
    const propsToUse = found.propsToUse;

    if (!ElementToUse) {
      console.warn("can't find component to display: " + JSON.stringify(contentProvider.peekCurrent()));
    } else {
      renderedElements.push(
        <ElementToUse
          key={contentProvider.idx}
          {...propsToUse}
          prev={contentProvider.peekPrev()}
          next={contentProvider.peekNext()}
          elementsLibrary={elementsLibrary}
        />
      );
    }

    contentProvider.advance(found.consumedElementsNumber);
  }

  return renderedElements;
}

interface ContentProvider {
  peekCurrent(): DocElementPayload | undefined;
  peekPrev(): DocElementPayload | undefined;
  peekNext(): DocElementPayload | undefined;
}

class ElementsContentProvider implements ContentProvider {
  idx: number = 0;

  constructor(readonly content: DocElementContent) {}

  peekCurrent(): DocElementPayload | undefined {
    return this.idx >= this.content.length ? undefined : this.content[this.idx];
  }

  peekNext(): DocElementPayload | undefined {
    return this.idx === this.content.length - 1 ? undefined : this.content[this.idx + 1];
  }

  peekPrev(): DocElementPayload | undefined {
    return this.idx === 0 ? undefined : this.content[this.idx - 1];
  }

  advance(step: number) {
    this.idx += step;
  }
}

function findRenderComponent(elementsLibrary: ElementsLibraryMap, contentProvider: ContentProvider) {
  const item = contentProvider.peekCurrent();
  const next = contentProvider.peekNext();

  // TODO generic way in elements library when more use-cases emerge
  if (isImageWithNumericAnnotations(item!, next)) {
    return imageWithNumericTextRenderComponent(elementsLibrary, item!, next!);
  }

  return {
    component: elementsLibrary[item!.type],
    propsToUse: item,
    consumedElementsNumber: 1,
  };
}

const ANNOTATED_IMAGE_TYPE = "AnnotatedImage";
const ORDERED_LIST_TYPE = "OrderedList";

function isImageWithNumericAnnotations(current: DocElementPayload, next?: DocElementPayload) {
  if (!next) {
    return false;
  }

  return (
    (isAnnotatedImage(current) && next.type === ORDERED_LIST_TYPE) ||
    (current.type === ORDERED_LIST_TYPE && isAnnotatedImage(next))
  );

  function isAnnotatedImage(item: DocElementPayload) {
    if (item.type !== ANNOTATED_IMAGE_TYPE) {
      return false;
    }

    // @ts-ignore
    return item.shapes.length > 0;
  }
}

function imageWithNumericTextRenderComponent(
  elementsLibrary: ElementsLibraryMap,
  item: DocElementPayload,
  next: DocElementPayload
) {
  return {
    component: elementsLibrary.AnnotatedImageWithOrderedList,
    propsToUse: {
      annotatedImageContent: findImageContent(),
      orderedListContent: findOrderedListContent(),
      isImageFirst: findIsImageFirst(),
    },
    consumedElementsNumber: 2,
  };

  function findImageContent() {
    return item.type === ANNOTATED_IMAGE_TYPE ? item : next;
  }

  function findOrderedListContent() {
    return item.type === ORDERED_LIST_TYPE ? item : next;
  }

  function findIsImageFirst() {
    return item.type === ANNOTATED_IMAGE_TYPE;
  }
}
