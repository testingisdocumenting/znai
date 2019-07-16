export default class Theme {
    constructor({name, elementsLibrary, presentationElementHandlers, themeClassName}) {
        this.name = name
        this.elementsLibrary = elementsLibrary
        this.presentationElementHandlers = presentationElementHandlers
        this.themeClassName = themeClassName
    }
}