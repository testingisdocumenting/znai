import React from 'react'

import './CliOutput.css'

const CliOutput = ({lines, chunkSize, fadedSize, highlight, isPresentation, slideIdx}) => {
    fadedSize = typeof fadedSize === 'undefined' ? 2 : fadedSize
    chunkSize = chunkSize || lines.length

    return (
        <div className="cli-output content-block">
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

        const linesBefore = startIdx > fadedSize ? lines.slice(startIdx - fadedSize, startIdx) : []
        const linesAfter = endIdx < lines.length ? lines.slice(endIdx, endIdx + fadedSize) : []
        const linesToShow = lines.slice(startIdx, endIdx)

        return [...renderLines(startIdx - fadedSize, linesBefore, "faded"),
            ...renderLines(startIdx, linesToShow),
            ...renderLines(endIdx, linesAfter, "faded")]
    }

    function renderLines(startIdx, lines, additionalClassName) {
        return lines.map((line, idx) => {
            const fullIdx = startIdx + idx
            const className = "output-line" + (isHighlighted() ? " highlight" : "") + " " + additionalClassName

            return <span key={fullIdx} className={className}>{line + "\n"}</span>

            function isHighlighted() {
                if (! highlight) {
                    return false
                }

                return highlight.indexOf(fullIdx) !== -1
            }
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
