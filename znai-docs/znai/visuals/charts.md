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

    :include-barchart: charts/game-activities.csv

:include-barchart: charts/game-activities.csv

:include-file: charts/game-activities.csv {autoTitle: true} 

Use `stack: true` to render multiple bars stacked

    :include-barchart: charts/game-activities.csv {stack: true}

:include-barchart: charts/game-activities.csv {stack: true}

Use `horizontal: true` and `height` parameter to change render direction

    :include-barchart: charts/game-activities.csv {stack: true, horizontal: true, height: 250}

:include-barchart: charts/game-activities.csv {stack: true, horizontal: true, height: 250}

# Line

To build a line chart use:

    :include-linechart: charts/genres.csv

:include-file: charts/genres.csv {autoTitle: true}

:include-linechart: charts/genres.csv

Use multiple CSV columns to add more lines

    :include-linechart: charts/daily-genres.csv

:include-file: charts/daily-genres.csv {autoTitle: true}

:include-linechart: charts/daily-genres.csv

Use numbers in first column to have a regular X, Y plots

    :include-linechart: charts/competitors.csv

:include-file: charts/competitors.csv {autoTitle: true}

:include-linechart: charts/competitors.csv

# Time Series

Use `time: true` to treat `X` as time series

:include-file: charts/time-series.csv {autoTitle: true} 

    :include-linechart: charts/time-series.csv {time: true}

# Legend 

Use `legend: true` to add legend to a chart

    :include-linechart: charts/daily-genres.csv {legend: true}

:include-linechart: charts/daily-genres.csv {legend: true}

# Wide Mode

Use `wide: true` to use available horizontal space

    :include-linechart: charts/daily-genres.csv {wide: true}

:include-linechart: charts/daily-genres.csv {wide: true}

Use in combination with height to fit larger charts

    :include-linechart: charts/daily-genres.csv {wide: true, height: 800}

:include-linechart: charts/daily-genres.csv {wide: true, height: 800}


# Inlined Data 

Use fence block plugin to inline chart data into markdown  

    ```piechart
    genre, preference
    RPG, 75
    Action, 50
    RTS, 40
    FPS, 50
    ```
  
```piechart
genre, preference
RPG, 75
Action, 50
RTS, 40
FPS, 50
```

    ```linechart {legend: true}
    day, RPG, RTS, FPS
    Monday, 100, 10, 0
    Tuesday, 50, 50, 20
    Wednesday, 10, 30, 50
    Thursday, 5, 5, 100
    Friday, 0, 100, 10
    ```

```linechart {legend: true}
day, RPG, RTS, FPS
Monday, 100, 10, 0
Tuesday, 50, 50, 20
Wednesday, 10, 30, 50
Thursday, 5, 5, 100
Friday, 0, 100, 10
```

    ```barchart
    genre, preference
    RPG, 75
    Action, 50
    RTS, 40
    FPS, 50
    ```

```barchart
genre, preference
RPG, 75
Action, 50
RTS, 40
FPS, 50
```

# Presentation

Charts automatically participate in slides.
Each chart becomes an individual slide.

Use `breakpoint` to add extra slides transitions.

    :include-linechart: charts/competitors.csv {
      legend: true,
      breakpoint: [17, 42]
    }

:include-linechart: charts/competitors.csv {
  legend: true,
  breakpoint: [17, 42]
}

To try it, press on `:icon: maximize` icon next to the **Presentation** header

    :include-piechart: charts/genres.csv {breakpoint: "Action"}

:include-piechart: charts/genres.csv {breakpoint: "Action"}

Use `all` as breakpoint value to create a slide for each textual entry 

    :include-barchart: charts/genres.csv {
      legend: true,
      breakpoint: "all"
    }

:include-barchart: charts/genres.csv {
  legend: true,
  breakpoint: "all"
}

# ECharts

`Znai` uses the beautiful and functional [EChart](https://echarts.apache.org/) library to render charts.
