class JsClass {
    usefulAction() {
        if (userId === "user-a") {
            // ...
        }

        if (canRead(userId)) {
            // ...
        }

        if (userId === "user-a") {
            // ...
            if (canRead(userId)) {
                // ...
            }
        }
    }
}
