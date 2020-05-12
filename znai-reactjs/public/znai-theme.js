(function() {
    var themeNameKey = 'znaiTheme';
    var darkThemeName = 'znai-dark';
    var lightThemeName = 'default';

    var znaiTheme = {
        changeHandlers: [],
        addChangeHandler(handler) {
            this.changeHandlers.push(handler);
        },
        removeChangeHandler(handler) {
            var idx = this.changeHandlers.indexOf(handler);
            this.changeHandlers.splice(idx, 1);
        },
        set(name) {
            this.name = name;
            document.body.className = 'theme-' + name;

            var idx = 0;
            var len = this.changeHandlers.length;
            for (; idx < len; idx++) {
                this.changeHandlers[idx](name);
            }
        },
        setExplicitly(name) {
            storeThemeName(name);
            this.set(name);
        },
        setExplicitlyIfNotSetAlready(name) {
            const themeName = getStoredThemeName();
            if (themeName) {
                return
            }

            this.setExplicitly(name)
        },
        toggle() {
            this.setExplicitly(this.name === lightThemeName ? darkThemeName : lightThemeName)
        }
    };

    var mediaThemeName = setLightMatchMediaListenerAndGetThemeName()
    var themeName = getStoredThemeName() || mediaThemeName;
    znaiTheme.set(themeName);

    window.znaiTheme = znaiTheme;

    function getStoredThemeName() {
        return localStorage.getItem(themeNameKey);
    }

    function storeThemeName(name) {
        return localStorage.setItem(themeNameKey, name);
    }

    function setLightMatchMediaListenerAndGetThemeName() {
        if (!window.matchMedia) {
            return darkThemeName;
        }

        var lightQuery = window.matchMedia('(prefers-color-scheme: light)');
        lightQuery.addListener(function (e) {
            const newThemeName = e.matches ? lightThemeName : darkThemeName;
            znaiTheme.setExplicitly(newThemeName);
        });

        return lightQuery.matches ? lightThemeName : darkThemeName;
    }
})()