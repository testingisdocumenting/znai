exports.removeCustomProps = removeCustomProps

function removeCustomProps(props) {
    const res = {...props}
    delete res.svg
    delete res.colors
    delete res.parentClassName

    return res
}