import React from 'react'
import Steps from './Steps'

import steps from './testSteps'

const StepsDemo = () => {
    return (
        <div className="report">
            <Steps steps={steps}/>
        </div>
    )
}

export default StepsDemo
