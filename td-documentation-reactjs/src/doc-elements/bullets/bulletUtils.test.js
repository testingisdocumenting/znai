import {startsWithIcon, extractIconId, removeIcon} from './bulletUtils'

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

    it("removes icon from content", () => {
        const originalSize = itemContent[0].content.length

        const withoutIcon = removeIcon(itemContent)

        expect(withoutIcon[0].content[0].type).toEqual("Emphasis")
        expect(itemContent[0].content.length).toEqual(originalSize)
    })
})