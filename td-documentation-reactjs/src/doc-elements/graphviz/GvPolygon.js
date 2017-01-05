import React, { Component } from 'react'

class GvPolygon extends Component {
    render() {
        const scaledDownPoints = scaleDown(this.props.points)
        return <polygon {...this.props} points={scaledDownPoints} />
    }
}

// make 4 points polygon slighlty smaller so arrows dont connect with the surface
// points="0,-73.5 0,-109.5 54,-109.5 54,-73.5 0,-73.5
// returns scaleX and scaleY
function scaleDown(points) {
    let coordPairs = points.split(' ')
    if (coordPairs.length !== 5) {
        return points
    }

    let x = []
    let y = []

    const collectXY = (pair) => {
        const split = pair.split(',')
        x.push(Number(split[0]))
        y.push(Number(split[1]))
    }

    coordPairs.forEach((pair) => collectXY(pair))
    const minX = Math.min(...x)
    const maxX = Math.max(...x)
    const minY = Math.min(...y)
    const maxY = Math.max(...y)

    const gap = 4
    x[0] += gap
    x[1] += gap
    x[2] -= gap
    x[3] -= gap
    x[4] += gap

    y[0] -= gap
    y[1] += gap
    y[2] += gap
    y[3] -= gap
    y[4] -= gap

    const newPoints = `${x[0]},${y[0]} ${x[1]},${y[1]} ${x[2]},${y[2]} ${x[3]},${y[3]} ${x[4]},${y[4]}`
    console.log("new points", newPoints)

    return newPoints
}

export default GvPolygon