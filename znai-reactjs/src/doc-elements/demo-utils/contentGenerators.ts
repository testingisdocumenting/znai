/*
 * Copyright 2022 znai maintainers
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

export function contentParagraph(isRightSide: boolean) {
  return {
    type: "Paragraph",
    content: [
      {
        text:
          "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though. Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs.",
        type: "SimpleText",
      },
    ],
    meta: {
      rightSide: isRightSide,
    },
  };
}

export function contentSingleLink(text: string) {
  return {
    type: "Paragraph",
    content: [
      {
        url: "#url",
        isFile: false,
        type: "Link",
        content: [
          {
            text,
            type: "SimpleText",
          },
        ],
      },
    ],
  };
}

export function contentParagraphSmall(isRightSide: boolean) {
  return {
    type: "Paragraph",
    content: [
      {
        text:
          "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though.",
        type: "SimpleText",
      },
    ],
    meta: {
      rightSide: isRightSide,
    },
  };
}

export function contentSnippet(isRightSide: boolean) {
  return {
    type: "Snippet",
    lang: "java",
    snippet: codeWithMethodCalls(),
    meta: {
      rightSide: isRightSide,
    },
  };
}

export function codeWithMethodCalls() {
  return (
    'http.get("/end-point", http.header("h1", "v1"), ((header, body) -> {\n' +
    '    body.get("price").should(equal(100));\n' +
    "}));"
  );
}

export const personApiParameters = [
  {
    anchorId: "prefix_firstName",
    name: "firstName",
    type: "string",
    description: [{ text: "first name", type: "SimpleText" }],
  },
  {
    anchorId: "prefix_lastName",
    name: "lastName",
    type: "string",
    description: [{ text: "last name", type: "SimpleText" }],
  },
  {
    anchorId: "prefix_score",
    name: "score",
    type: "integer",
    description: [{ text: "score accumulated over last year", type: "SimpleText" }],
  },
];

export function contentApiParameters() {
  return {
    type: "ApiParameters",
    parameters: personApiParameters,
    noWrap: true,
    title: "my params",
  };
}
