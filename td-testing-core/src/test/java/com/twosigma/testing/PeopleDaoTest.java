package com.twosigma.testing;

import com.twosigma.testing.data.table.TableData;
import org.junit.Test;

import static com.twosigma.testing.Ddjt.actual;
import static com.twosigma.testing.Ddjt.equal;
import static com.twosigma.testing.Ddjt.header;

/**
 * @author mykola
 */
public class PeopleDaoTest {
    private PeopleDao dao = new PeopleDao();

    @Test
    public void providesAccessToNewJoiners() {
        // ...

        TableData expected = header("id", "level", "monthsAtCompany").values(
                                    "bob",      3,   0,
                                    "smith",    4,   0);

        actual(dao.thisWeekJoiners()).should(equal(expected));
    }
}