package com.twosigma.testing.reporter;

/**
 * for values we already fetched we don't want report redundant "expecting value to equal" as there
 * is not going to be any delay to fetch a value
 *
 * @author mykola
 */
public enum StepReportOptions {
    SKIP_START,
    REPORT_ALL
}
