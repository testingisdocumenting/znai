package com.twosigma.testing;

import java.util.Arrays;
import java.util.List;

/**
 * @author mykola
 */
public class PeopleDao {
    public List<Person> thisWeekJoiners() {
        return Arrays.asList(new Person("bob", 3, 0),
                new Person("smith", 4, 0));
    }
}
