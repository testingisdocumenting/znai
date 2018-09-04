var myComponents = (function (exports,React) {
    'use strict';

    React = React && React.hasOwnProperty('default') ? React['default'] : React;

    function CustomComponentA(_ref) {
        var title = _ref.title;

        return React.createElement(
            'div',
            { className: 'custom-component-a' },
            title
        );
    }

    exports.CustomComponentA = CustomComponentA;

    return exports;

}({},React));
