# CSV

There is a directive to display table data like the one below. 

    :include-csv: table.csv 
    
Instead of aligning tables using one of the markdown extensions you can use your **CSV** editor of choice.

So the following csv file

:include-text-file: table.csv

renders as this table

:include-csv: table.csv

By default column width is auto calculated to fit the values inside. 
But you can control the width with the extra options 


    :include-csv: table.csv {Price: {width: 200}, "Description": {width: 400}}

:include-csv: table.csv {Price: {width: 200}, "Description": {width: 400}}

And if you need to change a column alignment from default *left* to *right* just add `align` property
    
    :include-csv: table.csv {Price: {width: 200, align: "right"}}

:include-csv: table.csv {Price: {width: 200, align: "right"}}