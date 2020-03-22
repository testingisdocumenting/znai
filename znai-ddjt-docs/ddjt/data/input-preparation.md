# Data Injection

Let's consider an example. 
`MarginCalculator` class with `calculateMargin` method that requires a list of `Transaction` objects.
 
One possible design of `MarginCalculator`:
* fetch required data by itself
* *Inject* `DAO` to decouple
 
```java
public class MarginCalculator {
    public MarginCalculator(TransactionsDao transactionsDao) {
    }
    
    public double calculate(List<String> ids) {
    }
}
```

Lets make design even simpler for testing:
* *Inject* required data

```java
public class MarginCalculator {
    public double calculate(List<Transactions> transactions) {
    }
}
```

This version is much easier to test:
* Less mocks/stubs
* Less wiring

# Input Encapsulation

Lets create *input* data required for a test.

:include-java: org/testingisdocumenting/testing/examples/margin/MarginCalculatorWithoutApiTest.java {entry: "marginShouldBeZeroIfNoLotsSet"}

What will happen to this test when we refactor `Transaction` class? For example *setters* can be removed in favor of
*Builder* pattern.

Our test is not about how to create `Transaction` instances. It is a about **logic of margin calculation**.
Our test must survive refactorings. 
In order to survive refactoring a *Test* must limit its exposure to implementation details.

Lets encapsulate `Transaction` creation

:include-java: org/testingisdocumenting/testing/examples/margin/MarginCalculatorWithBasicEncapsulationTest.java {entry: "createTransaction"}
 
If `Transaction` implementation details change we update one place and not every test that depends 
on this core domain object.

:include-java: org/testingisdocumenting/testing/examples/margin/MarginCalculatorWithBasicEncapsulationTest.java {entry: "marginShouldBeZeroIfNoLotsSet"}

# Table Data

A few problems with the way we implemented `createTransaction`:
* need to specify all the parameters
* no visible parameter names

Instead lets define test data using `TableData`

```tabs
Groovy: :include-groovy: org/testingisdocumenting/testing/examples/margin/MarginCalculatorWithGroovyTableDataTest.groovy {entry: "margin should be zero if no lots set"}
Java: :include-java: org/testingisdocumenting/testing/examples/margin/MarginCalculatorWithTableDataTest.java {entry: "marginShouldBeZeroIfNoLotsSet"}
```

`TableData` is a core class of this library. Consider it to be a list of maps on steroids. 
Let's define `createTransaction` in terms of TableData.


```tabs
Groovy: :include-groovy: org/testingisdocumenting/testing/examples/margin/MarginCalculatorWithGroovyTableDataTest.groovy {entry: "createTransactions"}
Java: :include-java: org/testingisdocumenting/testing/examples/margin/MarginCalculatorWithTableDataTest.java {entry: "createTransactions"}
```

Now we can:
* focus on test data
* skip properties and introduce defaults
* perform refactoring on `Transaction`
