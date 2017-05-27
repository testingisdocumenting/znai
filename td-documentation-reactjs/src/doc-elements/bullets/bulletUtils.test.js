import {startsWithIcon, extractIconId} from './bulletUtils'

const itemContent = [
    {
        "type": "Paragraph",
        "content": [
            {
                "type": "Icon",
                "id": "time"
            },
            {
                "type": "Emphasis",
                "content": [
                    {
                        "text": "Inject",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "text": " ",
                "type": "SimpleText"
            },
            {
                "code": "DAO",
                "type": "InlinedCode"
            },
            {
                "text": " to decouple",
                "type": "SimpleText"
            }
        ]
    }
]

describe("bulletUtils", () => {
    it("detects if bullet item starts with Icon", () => {
        expect(startsWithIcon(itemContent)).toBeTruthy()
    })

    it("extracts Icon id", () => {
        expect(extractIconId(itemContent)).toEqual("time")
    })
})