import {isPreviewEnabled} from '../docMeta'

export function imageAdditionalPreviewUrlParam(timestamp) {
    return isPreviewEnabled() && timestamp ?
        '?timestamp=' + timestamp:
        ''
}