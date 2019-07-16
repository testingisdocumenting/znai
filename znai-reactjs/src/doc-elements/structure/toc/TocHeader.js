import React from 'react'
import PanelCollapseButton from './PanelCollapseButton'

export default function TocHeader({docMeta, collapsed, onCollapseToggle, onHeaderClick}) {
    return (
        <div className="toc-panel-header">
            <div className="toc-panel-header-logo-and-title">
                <div className="mdoc-documentation-logo"/>
                <div className="toc-panel-header-title"
                     onClick={onHeaderClick}>
                    {docMeta.title + " " + docMeta.type}
                </div>
            </div>
            <PanelCollapseButton isCollapsed={collapsed} onClick={onCollapseToggle}/>
        </div>
    )
}