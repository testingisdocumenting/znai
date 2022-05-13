# Pie

To build a pie chart use:

    :include-piechart: charts/genres.csv

:include-file: charts/genres.csv {autoTitle: true}

:include-piechart: charts/genres.csv

# Bar

To build a bar chart use:

    :include-barchart: charts/genres.csv

:include-barchart: charts/genres.csv

:include-file: charts/genres.csv {autoTitle: true}

Add more columns to CSV data to use multiple bars per X axis tick

    :include-barchart: charts/multi-barchart.csv

:include-barchart: charts/multi-barchart.csv 

:include-file: charts/multi-barchart.csv {autoTitle: true} 

Use `stack: true` to render multiple bars stacked

    :include-barchart: charts/multi-barchart.csv {stack: true}

:include-barchart: charts/multi-barchart.csv {stack: true}

Use `horizontal: true` and `height` parameter to change render direction

    :include-barchart: charts/multi-barchart.csv {stack: true, horizontal: true, height: 300}

:include-barchart: charts/multi-barchart.csv {stack: true, horizontal: true, height: 300}


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

# ECharts

`Znai` uses the beautiful and functional [EChart](https://echarts.apache.org/) library to render charts.
