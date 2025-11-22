/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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