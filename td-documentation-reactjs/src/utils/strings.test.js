import {splitTextIntoLines} from './strings'

describe("string utils", () => {
    it("should split text into one line if threshold is not reached", () => {
        const lines = splitTextIntoLines("hello", 10)
        expect(lines).toEqual(["hello"])
    })

    it("should split text into two lines using spaces as dilimiter", () => {
        const lines = splitTextIntoLines("hello world to", 12)
        expect(lines).toEqual(["hello world", "to"])
    })

    it("should split text as soon as threshold is reached", () => {
        const lines = splitTextIntoLines("hello world to", 1)
        expect(lines).toEqual(["hello", "world", "to"])
    })
})