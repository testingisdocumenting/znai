// this is for convenient run from IDEA during dev
// instead of building full maven and run through plugin
// we inject this location for lookup paths to find resources

package znaiOverrides.org.junit;

// this is override file so preview for znai docs can be ran without assuming classpath
// Assert.java is used as an example of including sources from classpath in znai docs
// znai docs preview is ran through maven during build
// but during tests we run it using as CLI
//
public class Assert {

    /**
     * Asserts that two object arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with the given message. If
     * <code>expecteds</code> and <code>actuals</code> are <code>null</code>,
     * they are considered equal.
     *
     * @param message the identifying message for the {@link AssertionError} (<code>null</code>
     * okay)
     * @param expecteds Object array or array of arrays (multi-dimensional array) with
     * expected values.
     * @param actuals Object array or array of arrays (multi-dimensional array) with
     * actual values
     */
    public static void assertArrayEquals(String message, Object[] expecteds,
                                         Object[] actuals) {
        // this is override
    }

}
