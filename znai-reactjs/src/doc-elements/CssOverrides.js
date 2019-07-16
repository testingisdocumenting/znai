import React from 'react'

/**
 * Injects css at runtime based on the presence of static/css/custom.css file.
 * Applicable for on-premise mdoc hosting where there is a central hosting of documentation.
 * Various documentations can be built at different times.
 * At the time of their original build there was no custom.css, so they don't refer any customizations.
 * Docs need to be updated now to have custom overrides.
 *
 * It is most likely a temporary solution.
 */
// TODO remove after Landing Page TS release

function injectCustomCssLink() {
    const link = document.createElement('link')
    link.rel = 'stylesheet'
    link.type = 'text/css'
    link.href = '/static/css/custom.css'

    document.head.appendChild(link)
}

export {injectCustomCssLink}