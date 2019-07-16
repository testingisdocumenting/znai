import {
    containsInlinedComment,
    extractTextFromTokens,
    isInlinedComment,
    splitTokensIntoLines,
    trimComment
} from './codeUtils'
import {parseCode} from './codeParser';

const tokens = [{"type": "keyword", "content": "class"}, " ", {"type": "class-name", "content": ["Test"]}, " Test2 ", {"type": "punctuation", "content": "{"}, "\n",
    {"type": "comment", "content": "/*another \n comment line \nend of comment */"}, "\n    ",
    {"type": "keyword", "content": "var"}, " a  ", {"type": "operator", "content": "="}, " ", {"type": "number", "content": "2"}, {"type": "punctuation", "content": ";"}, " ", {"type": "comment", "content": "// comment line"}, "\n    ",
    {"type": "keyword", "content": "var"}, " b ", { "type": "operator", "content": "=" }, " a ", {"type": "operator", "content": "+"}, " ", {"type": "number", "content": "1"}, { "type": "punctuation", "content": ";" }, "       ", {"type": "comment", "content": "//          another comment"}, "\n    ",
    { "type": "keyword", "content": "var" }, " c ", {"type": "operator", "content": "="}, " ", {"type": "number", "content": "3"}, { "type": "punctuation", "content": ";" }, "         ", {"type": "comment", "content": "//             in two lines"}, "\n    ",
    { "type": "keyword", "content": "var" }, " d ", {"type": "operator", "content": "="}, " a ", {"type": "operator", "content": "+"}, " ", { "type": "number", "content": "1" }, {"type": "punctuation", "content": ";"}, "\n"]

const codeWithEmptyLines = `public class DocScaffolding {
    private final Path workingDir;

    private Map<String, List<String>> fileNameByDirName;`

describe("codeUtils", () => {
    it("split list of tokens into lists of tokens per line", () => {
        const lines = splitTokensIntoLines(tokens)
        expect(lines.length).toEqual(6)

        expect(lines[0]).toEqual([{"type": "keyword", "content": "class"}, " ", {
            "type": "class-name",
            "content": ["Test"]
        }, " Test2 ", {"type": "punctuation", "content": "{"}, "\n"])

        expect(lines[2]).toEqual(["    ", {"type": "keyword", "content": "var"}, " a  ", {"type": "operator", "content": "="}, " ", {"type": "number", "content": "2"}, {"type": "punctuation", "content": ";"}, " ", {"type": "comment", "content": "// comment line"}, "\n"])
    })

    it("creates separate empty lines", () => {
        const tokens = parseCode('java', codeWithEmptyLines)
        const lines = splitTokensIntoLines(tokens)
        expect(lines.length).toEqual(4)
        expect(lines[2]).toEqual(['\n'])
    })

    it("handles string token with new-line code in the middle", () => {
        const tokens = ['hello\n  world']
        const lines = splitTokensIntoLines(tokens)

        expect(lines.length).toEqual(2)
        expect(lines[0]).toEqual(['hello\n'])
        expect(lines[1]).toEqual(['  world'])
    })

    it("converts line of tokens to a simple text", () => {
        const code = 'public class PreviewServer {'
        const tokens = parseCode('java', code)
        const text = extractTextFromTokens(tokens)
        expect(text).toEqual(code)
    })

    it("detects if a token is an inlined comment", () => {
        const nonInlined = {"type": "comment", "content": "/*another \n comment line \nend of comment */"}
        const inlined = {"type": "comment", "content": "// comment line"}

        expect(isInlinedComment(nonInlined)).toBeFalsy()
        expect(isInlinedComment(inlined)).toBeTruthy()
    })

    it("detects if a list of tokens contains an inlined comment", () => {
        const tokens = ["    ", {"type": "keyword", "content": "var"}, " a  ", {"type": "operator", "content": "="}, " ", {"type": "number", "content": "2"}, {"type": "punctuation", "content": ";"}, " ", {"type": "comment", "content": "// comment line"}, "\n"]

        expect(containsInlinedComment(tokens)).toBeTruthy()
    })

    it("trims comment", () => {
        expect(trimComment("//  comment")).toEqual("comment")
    })
})

