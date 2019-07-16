import React from 'react';

import './CustomComponentA.css';

export function CustomComponentA({title}) {
    return (
        <div className="custom-component-a">{title}</div>
    );
}