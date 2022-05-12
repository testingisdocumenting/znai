# Pie

Given a `CSV` file...

:include-file: data.csv

..to build a pie chart use:

    :include-chart: data.csv {type: "Pie"}

:include-chart: data.csv {type: "Pie"}

You can change the style with the `innerRadius` property:

    :include-chart: data.csv {type: "Pie", innerRadius: 100} 

:include-chart: data.csv {type: "Pie", innerRadius: 100}

# Bar

To build a bar chart use:

    :include-barchart: data.csv

:include-barchart: data.csv

:include-file: data.csv {autoTitle: true}

Add more columns to CSV data to use multiple bars per X axis tick

:include-barchart: charts/multi-barchart.csv 

:include-file: charts/multi-barchart.csv {autoTitle: true} 


# Line

To build a line chart use:

    :include-chart: data.csv {type: "Line"}

:include-chart: data.csv {type: "Line"}

# Presentation Mode

In presentation mode, chart values for `Bar` and `Pie` appear one at a time.
To force all values to appear at once use:

    :include-meta: {allAtOnce: true}

# Victory Charts

`Znai` uses the [VictoryChart](https://github.com/FormidableLabs/victory-chart) library to render charts.
