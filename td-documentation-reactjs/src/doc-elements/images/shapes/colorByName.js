const colors = {
    red: {
        stroke: "#69140E",
        fill: "#A44200",
        text: "#69140E"
    },
    green: {
        stroke: "#35524A",
        fill: "#8DAB7F",
        text: "#35524A"
    },
    blue: {
        stroke: "#273043",
        fill: "#5D707F",
        text: "#273043"
    },
    yellow: {
        fill: "#FFE400",
        stroke: "#4A493E",
        text: "#AC9F39"
    }
}

export default (name) => {
    const color = colors[name]
    return color ? color : colors.red
}