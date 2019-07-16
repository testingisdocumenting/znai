/**

 DocElements may need additional resources that are global for each documentation page.
 We don't want to store the assets on every page payload and instead load them once from a separate javascript file.

 */
class GlobalAssets {
    constructor(assets) {
        this.assets = assets;
    }
}

const globalAssets = new GlobalAssets(window.globalAssets || {})

export {globalAssets}

export function setupGlobalAssets(assets) {
    globalAssets.assets = assets
}
