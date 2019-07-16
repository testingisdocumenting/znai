const colors = {
    white: {
        stroke: "#ddd",
        fill: "#e6e6e6",
        text: "#555"
    },
    red: {
        stroke: "#9e1e15",
        fill: "#d65100",
        text: "#fc957a"
    },
    green: {
        stroke: "#35524A",
        fill: "#8DAB7F",
        text: "#eee"
    },
    blue: {
        stroke: "#273043",
        fill: "#5D707F",
        text: "#eee"
    },
    yellow: {
        fill: "#FFE400",
        stroke: "#4A493E",
        text: "#eee"
    }
}

export default (name) => {
    const color = colors[name]
    return color ? color : colors.red
}