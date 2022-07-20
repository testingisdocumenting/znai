package org.testingisdocumenting.testing.examples

import org.junit.Test
import org.testingisdocumenting.webtau.data.table.TableData

import static org.testingisdocumenting.webtau.WebTauCore.*

class TestToDocExample {
    @Test
    void shouldRestrictAccountsActivity() {
        def rules = ["Account Type" | "Operation"    | "Restriction"] {
                    _________________________________________________
                        "SPB3"      | "Buy Options"  | "weekends only"
                        "TR"        | "Sell Futures" | "except holidays"
                        "BOSS"      | "Buy Stocks"   | "none" }

        validateRules(rules)
    }

    private static void validateRules(TableData rules) {
        doc.capture("account-rules", rules) // capture table data as JSON
    }
}
