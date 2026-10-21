package com.swaglabs.reporting;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ReportListener implements ITestListener, ISuiteListener {

    private static final Logger logger = LoggerManager.getLogger(ReportListener.class);

    private static final HtmlReporter htmlReporter = new HtmlReporter();

    @Override
    public void onTestSuccess(ITestResult result) {
        String tcId = result.getMethod().getMethodName();
        htmlReporter.logPass(tcId);
        logger.info("ReportListener: PASS — " + tcId);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String tcId = result.getMethod().getMethodName();
        String reason = result.getThrowable() != null
                ? result.getThrowable().getMessage()
                : "Unknown failure";
        htmlReporter.logFail(tcId, reason);
        logger.error("ReportListener: FAIL — " + tcId + " | " + reason);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String tcId = result.getMethod().getMethodName();
        htmlReporter.logFail(tcId, "SKIPPED");
        logger.warn("ReportListener: SKIPPED — " + tcId);
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("ReportListener: Suite finished. Generating HTML report...");
        htmlReporter.printSummary();
    }
}
