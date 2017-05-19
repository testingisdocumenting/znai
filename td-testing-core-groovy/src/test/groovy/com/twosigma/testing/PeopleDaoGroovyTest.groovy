package com.twosigma.testing

import org.junit.Test

/**
 * @author mykola
 */
class PeopleDaoGroovyTest {
    private PeopleDao dao = new PeopleDao();

    @Test
    void "provides access to new joiners"() {
        // ...

        dao.thisWeekJoiners().should == ["id"    | "level" | "monthsAtCompany"] {
                                         ______________________________________
                                         "bob"   | 3       | 0
                                         "smith" | 4       | 0  }
    }
}
