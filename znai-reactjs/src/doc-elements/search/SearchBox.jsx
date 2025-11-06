const React = require('react');

const SearchBox = ({ onSearch }) => {
    const inputRef = React.useRef(null);

    React.useEffect(() => {
        if (inputRef.current) {
            inputRef.current.focus();
        }
    }, []);

    const handleChange = (e) => {
        if (onSearch) {
            onSearch(e.target.value);
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter' && onSearch) {
            onSearch(inputRef.current?.value || '');
        }
    };

    return React.createElement('div', 
        { className: "znai-search-popup-input-box" },
        React.createElement('input', {
            ref: inputRef,
            placeholder: "Type terms to search...",
            onChange: handleChange,
            onKeyPress: handleKeyPress
        })
    );
};

module.exports = SearchBox;