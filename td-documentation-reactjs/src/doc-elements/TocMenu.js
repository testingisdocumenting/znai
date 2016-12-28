import React from 'react';

const Item = ({sectionTitle, title, fileName, isSelected, onClickHandler}) => {
    const className = "toc-item " + (isSelected ? "selected" : "");
    return (
        <div className={className} onClick={
            () => onClickHandler(sectionTitle, fileName)}>{title}</div>
    );
};

const Section = ({sectionTitle, items, selected, onClickHandler}) => {
    return (<div className="toc-section">
        <div className="title">{sectionTitle}</div>
        {items.map((item) => <Item key={item.fileName}
                                   sectionTitle={sectionTitle}
                                   isSelected={sectionTitle === selected.sectionTitle && item.fileName === selected.fileName}
                                   onClickHandler={onClickHandler} {...item} />)}
    </div>);
};

const TocMenu = ({toc, selected, onClickHandler}) => {
    selected = selected || {sectionTitle: "", fileName: ""};
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