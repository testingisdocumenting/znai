import * as fapi from 'flight-api'

function bookFlight(flightInfo) {
    const confirmation = fapi.book(flightInfo)
    //...
}

function flightStatus(id) {
    const fullStatus = fapi.query(id)
    return fullStatus.shortStatus
}