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

This is how list could be created in Java

:include-java: com/twosigma/testing/MarginCalculator.java {entry: "calculate"}

# Input Encapsulation

Lets create *input* data required for a test.

:include-java: com/twosigma/testing/MarginCalculatorWithoutApiTest.java {entry: "marginShouldBeZeroIfNoLotsSet"}

What will happen to this test when we refactor `Transaction` class? Setters can be replaced with constructor or a *Builder* pattern.

Our test is not about how to create `Transaction` instances. It is a about **logic of margin calculation**.
Our test must survive refactorings. 
In order to survive refactoring a *Test* must limit its exposure to implementation details.

Lets encapsulate `Transaction` creation

:include-java: com/twosigma/testing/MarginCalculatorWithBasicEncapsulationTest.java {entry: "createTransaction"}
 
If `Transaction` implementation details change we update one place and not every test that depends 
on this core domain object.

:include-java: com/twosigma/testing/MarginCalculatorWithBasicEncapsulationTest.java {entry: "marginShouldBeZeroIfNoLotsSet"}

# Table Data

A few problems with the way we implemented `createTransaction`:
* need to specify all the parameters
* no visible parameter names 