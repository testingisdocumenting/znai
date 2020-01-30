/**
 * Top level conceptual description of a {@link CustomDomain} problem.
 * <p>
 * To avoid <b>copy & paste</b> of the content consider to re-use information.
 */
class HelloWorld {
    /**
     * Each year we hire students from different universities to increase
     * <code>diversity</code>
     */
    private int numberOfStudents;

    /**
     * Conceptual description of a <i>Domain</i> problem.
     * <p>
     * It will work only if you put high level description here and
     * <b>not</b> implementation details.
     *
     * @param p1 important parameter of something
     * @param p2 sample <i>offset</i> according to the rules of the universe
     * @return name of the <b>best</b> sample
     */
    public String sampleMethod(String p1, int p2) {
        validate();
        process(p2); // important comment
        notifyAll(p1); // very important

        return bestSample();
    }

    public void sampleMethod(Map<String, Integer> p1, int p2, boolean isActive) {
        // overloaded method
    }

    /**
     * @param trader trader that performs action
     * @param transaction transaction to perform action on
     */
    public void importantAction(Trader trader, Transaction transaction) {
        // TODO important
    }

    public Data createData() {
        // create data
    }
}