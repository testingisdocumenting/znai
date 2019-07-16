import React from 'react'
import {themeRegistry} from './ThemeRegistry'

export default class WithTheme extends React.Component {
    render() {
        const theme = themeRegistry.currentTheme

        const themeClassName = 'with-theme' + (theme.themeClassName ? ' theme-' + theme.themeClassName : '')
            return (
                <div className={themeClassName}>
                    {this.props.children(theme)}
                </div>
            )
    }

    componentDidMount() {
        themeRegistry.addOnThemeChangeListener(this.onThemeChange)
    }

    componentWillUnmount() {
        themeRegistry.removeOnThemeChangeListener(this.onThemeChange)
    }

    onThemeChange = () => {
        this.forceUpdate()
    }
}
