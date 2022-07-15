# Method/Function Definition

Use `python-method` to include function/method definition with signature, documentation and parameters list

    :include-python-method: fin.money.render_money

:include-python-method: fin.money.render_money

:include-file: fin/money.py {autoTitle: true, highlight: "render_money"}

# Class Definition

To define a class use `python-class` include plugin

    :include-python-class: fin.money.Money 

Note: Once you define `Money` class, method arguments and parameter descriptions with the same type will automatically link to the place of definition

:include-python-class: fin.money.Money

# Inheritance

Example of inheritance

:include-file: department.py {autoTitle: true}

:include-file: executive_department.py {autoTitle: true}

    :include-python-class: executive_department.WorkerCTO
    :include-python-class: department.Worker

Note: Base classes that are defined somewhere in the documentation will turn into links

:include-python-class: executive_department.WorkerCTO
:include-python-class: department.Worker 
