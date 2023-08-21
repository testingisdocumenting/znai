class JsClass {
    usefulAction() {
        if (userId === "user-a") {
            if (canRead(userId)) {
                // ...
            }
        }
        if (userId === "user-b") {
            if (canWrite(userId)) {
                // ...
            }
        }
    }
}
