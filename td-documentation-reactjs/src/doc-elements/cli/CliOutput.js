import React from 'react'

import './CliOutput.css'

const CliOutput = ({lines, chunkSize, fadedSize, highlight, isPresentation, slideIdx}) => {
    const fadedLinesCount = fadedSize || 2

    return (
        <div className="cli-output">
            <pre>
                <code>
                    {isPresentation ? renderPresentationLines() : renderLines(0, lines, "")}
                </code>
            </pre>
        </div>
    )

    function renderPresentationLines() {
        const startIdx = chunkSize * slideIdx
        const endIdx = startIdx + chunkSize

        const linesBefore = startIdx > fadedLinesCount ? lines.slice(startIdx - fadedLinesCount, startIdx) : []
        const linesAfter = endIdx < lines.length ? lines.slice(endIdx, endIdx + fadedLinesCount) : []
        const linesToShow = lines.slice(startIdx, endIdx)

        return [...renderLines(startIdx - fadedLinesCount, linesBefore, "faded"),
            ...renderLines(startIdx, linesToShow),
            ...renderLines(endIdx, linesAfter, "faded")]
    }

    function renderLines(startIdx, lines, additionalClassName) {
        return lines.map((line, idx) => {
            const fullIdx = startIdx + idx
            const isHighlighted = highlight && highlight.indexOf(fullIdx) !== -1
            const className = "output-line" + (isHighlighted ? " highlight" : "") + " " + additionalClassName

            return <span key={fullIdx} className={className}>{line + "\n"}</span>
        })
    }
}

const presentationCliOutput = {component: CliOutput,
    numberOfSlides: ({lines, chunkSize}) => {
        if (chunkSize) {
            const numberOfChunks = lines.length / chunkSize | 0
            return lines.length % chunkSize === 0 ? numberOfChunks : numberOfChunks + 1
        }

        return 1
    }
}

export {CliOutput, presentationCliOutput}
