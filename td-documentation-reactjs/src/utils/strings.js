function splitTextIntoLines(text, thresholdCharCount) {
    if (text.length < thresholdCharCount) {
        return [text]
    }

    const words = text.split(" ")
    const result = []
    let runningLength = 0
    let runningWords = []

    words.forEach(word => {
        runningLength += word.length + 1 // one for space
        runningWords.push(word)

        if (runningLength >= thresholdCharCount) {
            flush()
        }
    })

    flush()

    return result

    function flush() {
        if (runningWords.length) {
            result.push(runningWords.join(" "))
        }

        runningWords = []
        runningLength = 0
    }
}

export {splitTextIntoLines}