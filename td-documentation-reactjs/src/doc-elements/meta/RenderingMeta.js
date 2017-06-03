class RenderingMeta {
    params_ = {}

    register(params) {
        const newMeta = new RenderingMeta()
        newMeta.params_ = {...this.params_, ...params}

        return newMeta
    }

    byType(type) {
        return this.params_[type]
    }

    paramValue(key) {
        const result = this.params_[key]
        return (typeof result === 'undefined') ? null : result
    }
}

export default RenderingMeta
