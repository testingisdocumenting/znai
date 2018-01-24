# Breaching Encapsulation

In order to use mocking frameworks you need to know what is going on within a component under test. 
You essentially need to breach encapsulation. Test's exposure to implementation details makes test less likely to survive a refactoring.

Let's look at a simple example.

:include-java: com/twosigma/testing/components/GamesShop.java {entry: "GamesShop"}

In order to write a test for `GamesShop` component, your test needs to know what methods are being called and what is
their signature.

-- TODO mock sample --

Let's do a simple refactoring. We will change `PaymentService` to accept `account` directly instead of `walletId`.
Because of this change our test that sets mocks expectations is broken now. 
Moreover All the other tests that prepare `PaymentService` are broken as well.  

# Violating SRP

Our test responsibility is to assert functionality of our component. The only reason we should change our test is 
when functionality we test is changed (Single Responsibility Principle).
 
*Refactoring* by definition should not change the functionality of our application, but yet we are forced to change our tests. 
     

# Cryptic Expectations

# Low Data Coverage

# Expensive Maintenance    
