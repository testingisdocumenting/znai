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
import { describe, it, expect } from 'vitest';

import {
    collapseCommentsAboveToMakeCommentOnTheCodeLine,
    containsInlinedComment,
    enhanceMatchedTokensWithMeta,
    extractTextFromTokens,
    findTokensThatMatchExpressions,
    isCommentToken, removeCommentsFromEachLine,
    splitTokensIntoLines,
    trimComment
} from "./codeUtils";

import {parseCode} from './codeParser';

const tokens = [{"type": "keyword", "content": "class"}, " ", {
    "type": "class-name",
    "content": ["Test"]
}, " Test2 ", {"type": "punctuation", "content": "{"}, "\n",
    {"type": "comment", "content": "/*another comment line end of comment */"}, "\n    ",
    {"type": "keyword", "content": "var"}, " a  ", {"type": "operator", "content": "="}, " ", {
        "type": "number",
        "content": "2"
    }, {"type": "punctuation", "content": ";"}, " ", {"type": "comment", "content": "// comment line"}, "\n    ",
    {"type": "keyword", "content": "var"}, " b ", {"type": "operator", "content": "="}, " a ", {
        "type": "operator",
        "content": "+"
    }, " ", {"type": "number", "content": "1"}, {"type": "punctuation", "content": ";"}, "       ", {
        "type": "comment",
        "content": "//          another comment"
    }, "\n    ",
    {"type": "keyword", "content": "var"}, " c ", {"type": "operator", "content": "="}, " ", {
        "type": "number",
        "content": "3"
    }, {"type": "punctuation", "content": ";"}, "         ", {
        "type": "comment",
        "content": "//             in two lines"
    }, "\n    ",
    {"type": "keyword", "content": "var"}, " d ", {"type": "operator", "content": "="}, " a ", {
        "type": "operator",
        "content": "+"
    }, " ", {"type": "number", "content": "1"}, {"type": "punctuation", "content": ";"}, "\n"]

const codeWithEmptyLines = `public class DocScaffolding {
    private final Path workingDir;

    private Map<String, List<String>> fileNameByDirName;`

describe("codeUtils", () => {
    describe("split into lines", () => {
        it("split list of tokens into lists of tokens per line", () => {
            const lines = splitTokensIntoLines(tokens)
            expect(lines.length).toEqual(6)

            expect(lines[0]).toEqual([
                {'content': 'class', 'type': 'keyword'},
                ' ',
                {'content': ['Test'], 'type': 'class-name'},
                ' ',
                'Test2 ',
                {'content': '{', 'type': 'punctuation'}])

            expect(lines[2]).toEqual([
                '    ',
                {'content': 'var', 'type': 'keyword'},
                ' ',
                'a  ',
                {'content': '=', 'type': 'operator'},
                ' ',
                {'content': '2', 'type': 'number'},
                {'content': ';', 'type': 'punctuation'},
                ' ',
                {'content': '// comment line', 'type': 'comment'}
            ])
        })

        it("creates separate empty lines", () => {
            const tokens = parseCode('java', codeWithEmptyLines)
            const lines = splitTokensIntoLines(tokens)
            expect(lines.length).toEqual(4)
            expect(lines[2]).toEqual([])
        })

        it("handles string token with new-line code in the middle", () => {
            const tokens = ['hello\n  world']
            const lines = splitTokensIntoLines(tokens)

            expect(lines.length).toEqual(2)
            expect(lines[0]).toEqual(['hello'])
            expect(lines[1]).toEqual([
                '  ',
                'world'])
        })

        it("converts text token with spacing into separate spacing token and text token", () => {
            const tokens = parseCode('java', ' http.get\n' +
                '    body.get("price") {\n' +
                '    body.get("id") {\n')
            const lines = splitTokensIntoLines(tokens)

            expect(lines).toEqual([
                [
                    ' ',
                    'http',
                    {type: 'punctuation', content: '.'},
                    'get'
                ],
                [
                    '    ',
                    'body',
                    {type: 'punctuation', content: '.'},
                    {type: 'function', content: 'get'},
                    {type: 'punctuation', content: '('},
                    {type: 'string', content: '"price"'},
                    {type: 'punctuation', content: ')'},
                    ' ',
                    {type: 'punctuation', content: '{'},
                ],
                [
                    '    ',
                    'body',
                    {type: 'punctuation', content: '.'},
                    {type: 'function', content: 'get'},
                    {type: 'punctuation', content: '('},
                    {type: 'string', content: '"id"'},
                    {type: 'punctuation', content: ')'},
                    ' ',
                    {type: 'punctuation', content: '{'},
                ]])
        })

        it("handles markdown tokens with multiple end of line in between", () => {
            const tokens = parseCode('markdown', '# Server Configuration\n\n' +
              ':include-file: config/server.config\n\nNote: hello world\n')

            const lines = splitTokensIntoLines(tokens)
            expect(lines).toEqual([
                [ { type: 'title', content: [{type: 'punctuation', content: '#'}, ' Server Configuration'] } ],
                [],
                [ ':include-file: config/server.config' ],
                [],
                [ 'Note: hello world' ]
            ])
        });
    });

    it("converts line of tokens to a simple text", () => {
        const code = 'public class PreviewServer {'
        const tokens = parseCode('java', code)
        const text = extractTextFromTokens(tokens)
        expect(text).toEqual(code)
    })

    it("finds token idx that match expression", () => {
        const tokensCalls = parseCode('java', 'http.get("/end-point", http.header("h1", "v1"), ((header, body) -> {')
        const resultCalls = findTokensThatMatchExpressions(tokensCalls, ['http.header', 'http.get', 'nonexiting'])
        expect(resultCalls).toEqual({'http.header': [6, 8], 'http.get': [0, 2]})

        const tokensClass = parseCode('java', 'public void myMethod(SuperType a, AnotherType b) {}')
        const resultClass = findTokensThatMatchExpressions(tokensClass, ['SuperType', 'AnotherType', 'nonexiting'])
        expect(resultClass).toEqual({SuperType: [6, 6], AnotherType: [10, 10]})
    })

    it("finds token idx ignoring quotes", () => {
        const tokens = ["hello", {type: "key", content: '"trader"'}]
        const result = findTokensThatMatchExpressions(tokens, ['trader'])
        expect(result).toEqual({'trader': [1, 1]})
    })

    it("enhances tokens with on click information", () => {
        const tokens = parseCode('java', 'http.get("/end-point", http.header("h1", "v1"), ((header, body) -> {')
        const lines = splitTokensIntoLines(tokens)
        const enhancedTokens = enhanceMatchedTokensWithMeta(lines[0], ['http.header', 'http.get'], () => 'link', (reference) => '@' + reference)

        expect(enhancedTokens).not.toBe(tokens)
        expect(enhancedTokens).toEqual([ { content: 'http', link: '@http.get', type: 'text link' },
            { type: 'punctuation link', content: '.', link: '@http.get' },
            { type: 'function link', content: 'get', link: '@http.get' },
            { type: 'punctuation', content: '(' },
            { type: 'string', content: '"/end-point"' },
            { type: 'punctuation', content: ',' },
            ' ',
            { content: 'http', type: 'text link', link: '@http.header' },
            { type: 'punctuation link', content: '.', link: '@http.header' },
            { type: 'function link', content: 'header', link: '@http.header' },
            { type: 'punctuation', content: '(' },
            { type: 'string', content: '"h1"' },
            { type: 'punctuation', content: ',' },
            ' ',
            { type: 'string', content: '"v1"' },
            { type: 'punctuation', content: ')' },
            { type: 'punctuation', content: ',' },
            ' ',
            { type: 'punctuation', content: '(' },
            { type: 'punctuation', content: '(' },
            'header',
            { type: 'punctuation', content: ',' },
            ' ',
            'body',
            { type: 'punctuation', content: ')' },
            ' ',
            { type: 'operator', content: '->' },
            ' ',
            { type: 'punctuation', content: '{' }])
    })

    it("should tokenize groovy type based declared variable", () => {
        const tokens = parseCode("groovy", "TableData table = createTable()\ntable.should == []")
        const lines = splitTokensIntoLines(tokens)
        const enhancedTokensLineOne = enhanceMatchedTokensWithMeta(lines[0], ["TableData"], () => 'link', (reference) => '@' + reference)
        const enhancedTokensLineTwo = enhanceMatchedTokensWithMeta(lines[1], ["should"], () => 'link', (reference) => '@' + reference)

        expect(enhancedTokensLineOne).toEqual([
              { content: 'TableData', link: '@TableData', type: 'text link' },
              ' ',
              'table',
              ' ',
              { type: 'operator', content: '=' },
              ' ',
              { type: 'function', content: 'createTable' },
              { type: 'punctuation', content: '(' },
              { type: 'punctuation', content: ')' }
          ]
        )

        expect(enhancedTokensLineTwo).toEqual([
            'table',
            { type: 'punctuation', content: '.' },
            { content: 'should', link: '@should', type: 'text link' },
            ' ',
            { type: 'operator', content: '==' },
            ' ',
            { type: 'punctuation', content: '[' },
            { type: 'punctuation', content: ']' }
        ])
    });

    it("splits java multi line comment into separate lines", () => {
        const tokens = parseCode('java', `  /** hello
  multi line
  comment
  */

class MyClass {
}
`)
        const lines = splitTokensIntoLines(tokens)

        expect(lines).toEqual([
            ['  ', { type: 'comment', content: '/** hello' }],
            [{ type: 'comment', content: '  multi line' }],
            [{ type: 'comment', content: '  comment' }],
            [{ type: 'comment', content: '  */' }],
            [],
            [
                { type: 'keyword', content: 'class' },
                ' ',
                { type: 'class-name', content: ['MyClass'] },
                ' ',
                { type: 'punctuation', content: '{' }
            ],
            [ { type: 'punctuation', content: '}' } ]
        ])
    })

    it("splits python multi line comment into separate lines", () => {
        const tokens = parseCode('python', ` def print_money(amount: int, message: str = ""):
    """
    print money with a given message
    Parameters
    --------------
    amount:
      amount to print
    """

    print("printing money")
`)
        const lines = splitTokensIntoLines(tokens)

        console.warn(lines)
    })

    describe('inlined comments', () => {
        it("detects if a token is an inlined comment", () => {
            const nonInlined = {"type": "comment", "content": "/*another \n comment line \nend of comment */"}
            const inlined = {"type": "comment", "content": "// comment line"}

            expect(isCommentToken(nonInlined)).toBeTruthy()
            expect(isCommentToken(inlined)).toBeTruthy()
        })

        it("detects if a list of tokens contains an inlined comment", () => {
            const tokens = ["    ", {"type": "keyword", "content": "var"}, " a  ", {
                "type": "operator",
                "content": "="
            }, " ", {"type": "number", "content": "2"}, {"type": "punctuation", "content": ";"}, " ", {
                "type": "comment",
                "content": "// comment line"
            }, "\n"]

            expect(containsInlinedComment(tokens)).toBeTruthy()
        })

        it("trims comment", () => {
            expect(trimComment("//comment")).toEqual("comment")
            expect(trimComment("//  comment")).toEqual("comment")
            expect(trimComment("#comment")).toEqual("comment")
            expect(trimComment("#  comment")).toEqual("comment")
            expect(trimComment("(* comment *)")).toEqual("comment")
        })

        it('collapses multi line comment and attaches it to the next code line', () => {
            const tokens = parseCode('python', 'def my_func():\n' +
              '  # comment line one \n' +
              '  # comment line two  \n' +
              '  a = 2    \n\n\n' +
              '  # comment line three \n' +
              '  a = 4')

            const lines = splitTokensIntoLines(tokens)
            const collapsed = collapseCommentsAboveToMakeCommentOnTheCodeLine(lines)

            expect(collapsed).toEqual([
                [
                    { type: 'keyword', content: 'def' },
                    ' ',
                    { type: 'function', content: 'my_func' },
                    { type: 'punctuation', content: '(' },
                    { type: 'punctuation', content: ')' },
                    { type: 'punctuation', content: ':' },
                ],
                [
                    '  ',
                    'a ',
                    { type: 'operator', content: '=' },
                    ' ',
                    { type: 'number', content: '2' },
                    { type: 'comment', content: 'comment line one comment line two' }
                ],
                [],
                [],
                [
                    '  ',
                    'a ',
                    { type: 'operator', content: '=' },
                    ' ',
                    { type: 'number', content: '4' },
                    { type: 'comment', content: 'comment line three' }
                ]
            ])
        })

        it('removes comment lines and tokens', () => {
            const tokens = parseCode('python', 'def my_func():\n' +
              '  # comment line one \n' +
              '  # comment line two  \n' +
              '  a = 2 # explanation A \n' +
              '  # comment line three \n' +
              '  a = 4 \n')

            const lines = splitTokensIntoLines(tokens)

            const withoutComments = removeCommentsFromEachLine(lines)
            expect(withoutComments).toEqual([
                  [
                      { type: "keyword", content: "def" },
                      " ",
                      { type: "function", content: "my_func" },
                      { type: "punctuation", content: "(" },
                      { type: "punctuation", content: ")" },
                      { type: "punctuation", content: ":" }
                  ],
                  [
                      "  ",
                      "a ",
                      { type: "operator", content: "=" },
                      " ",
                      { type: "number", content: "2" }
                  ],
                  [
                      "  ",
                      "a ",
                      { type: "operator", content: "=" },
                      " ",
                      { type: "number", content: "4" }
                  ]
              ]
            )
        })
    })
})

