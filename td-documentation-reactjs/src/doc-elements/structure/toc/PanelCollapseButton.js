import React from 'react'

import './PanelCollapseButton.css'

export default function PanelCollapseButton({isCollapsed, onClick}) {
    const className = isCollapsed ?
        'toc-panel-expand-button':
        'toc-panel-collapse-button'

    return (
        <div className={className} onClick={onClick}>
            <svg xmlns="http://www.w3.org/2000/svg" width="9" height="14" viewBox="0 0 9 14">
                <path fillRule="evenodd"
                      d="M6.93 13.23L0 6.61 6.93 0l1.48 1.532L3.305 6.65l5.107 4.977z"/>
            </svg>
        </div>
    )
}
