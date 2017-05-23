import React from 'react'

class RenderingMeta {
    paramsByType_ = {}

    register(type, params) {
        const newMeta = new RenderingMeta()
        newMeta.paramsByType_[type] = params

        return newMeta
    }

    byType(type) {
        return this.paramsByType_[type]
    }

    typeParam(type, param) {
        const params = this.paramsByType_[type];
        return (typeof params === 'undefined') ? null : params[param]
    }
}

export default RenderingMeta
