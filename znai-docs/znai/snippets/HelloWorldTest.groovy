class HelloWorldTest {
    @Test
    void "should calculate risk based on epsilon"() {
        generateStatement(price: 10, quantity: 10, epsilon: 2)
        calcRisk().should == 108
    }
}