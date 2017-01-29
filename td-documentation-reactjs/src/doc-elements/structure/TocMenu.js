import React from 'react';

const Item = ({pageTitle, dirName, fileName, isSelected, onClickHandler}) => {
    const className = "toc-item " + (isSelected ? "selected" : "");
    return (
        <div className={className} onClick={
            () => onClickHandler(dirName, fileName)}>{pageTitle}</div>
    );
};

const Section = ({sectionTitle, dirName, items, selected, onClickHandler}) => {
    return (<div className="toc-section">
        <div className="title">{sectionTitle}</div>
        {items.map((item) => <Item key={item.fileName}
                                   sectionTitle={sectionTitle}
                                   dirName={dirName}
                                   isSelected={dirName === selected.dirName && item.fileName === selected.fileName}
                                   onClickHandler={onClickHandler} {...item} />)}
    </div>);
};

const TocMenu = ({toc, selected, onClickHandler}) => {
    selected = selected || {dirName: "", fileName: ""};

    return (
        <div className="toc-menu">
            {toc.map((sectionEntry) =>
                <Section key={sectionEntry.sectionTitle}
                    selected={selected}
                    onClickHandler={onClickHandler}
                    {...sectionEntry} />)}
        </div>
    );
};

export default TocMenu