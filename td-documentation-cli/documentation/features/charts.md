# Pie

Given a `CSV` file

:include-file: data.csv

To build a pie chart use

    :include-chart: data.csv {type: "Pie"}

:include-chart: data.csv {type: "Pie"}

To change inner radius use

    :include-chart: data.csv {type: "Pie", innerRadius: 100} 

:include-chart: data.csv {type: "Pie", innerRadius: 100}

# Bar

To build a vertical chart use

    :include-chart: data.csv

:include-chart: data.csv

To build a horizontal chart use

    :include-chart: data.csv {horizontal: true}

:include-chart: data.csv {horizontal: true}

# Line

To build a line chart use

    :include-chart: data.csv {type: "Line"}

:include-chart: data.csv {type: "Line"}

# Presentation Mode

In presentation mode chart values for `Bar` and `Pie` appear one at a time.
To force all values to appear at once use

    :include-meta: {allAtOnce: true}



