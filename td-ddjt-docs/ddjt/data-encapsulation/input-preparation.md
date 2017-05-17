# Data Injection

Test flow is
* Input preparation
* Trigger
* Output validation

Quite often input preparation step takes a lot of effort. 
Partially because Java API is cumbersome and partially because test writers are focused on a wrong thing.

Components being designed so that fetch data themselves.
 
Let's consider an example. 
We have a function `calculateExposure` that accepts a list of `Trade` objects. 
In order to test it we need to create a list of objects.

This is how list could be created in Java

```java
    Trade t = new Trade();
    t.setId("id1")
    
```

# Creation Encapsulation