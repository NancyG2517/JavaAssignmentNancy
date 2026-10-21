package com.swaglabs.reporting;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseReporter implements Reporter {

    protected static final Logger logger = LoggerManager.getLogger(BaseReporter.class);

    private int totalTests  = 0;
    private int passedTests = 0;
    private int failedTests = 0;

    protected void recordPass() {
        totalTests++;
        passedTests++;
        logger.debug("DEBUG: [" + getClass().getSimpleName() + "] " +
                "Counters → total=" + totalTests + ", passed=" + passedTests);
    }

    protected void recordFail() {
        totalTests++;
        failedTests++;
        logger.debug("DEBUG: [" + getClass().getSimpleName() + "] " +
                "Counters → total=" + totalTests + ", failed=" + failedTests);
    }

    @Override
    public void printSummary() {
        System.out.println();
        System.out.println("==== TEST SUMMARY ====");
        System.out.println("Reporter    : " + this.getClass().getSimpleName());
        System.out.println("Total Tests : " + totalTests);
        System.out.println("Passed      : " + passedTests);
        System.out.println("Failed      : " + failedTests);
        System.out.println("======================");
        System.out.println();

        logger.info("[" + getClass().getSimpleName() + "] Summary — " +
                "Total=" + totalTests + " | Passed=" + passedTests +
                " | Failed=" + failedTests);
    }

    public int getTotalTests()  { return totalTests;  }

    public int getPassedTests() { return passedTests; }

    public int getFailedTests() { return failedTests; }
}
