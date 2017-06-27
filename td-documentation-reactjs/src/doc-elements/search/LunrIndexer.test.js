import LunrIndexer from './LunrIndexer'
import testData from './LunrIndexer.testdata'

describe("LunrIndexer", () => {
    it("should create from pages json", () => {
        const indexer = LunrIndexer.createWithPages(testData.allPages);
        const result = indexer.search("Groovy API")
    })
})