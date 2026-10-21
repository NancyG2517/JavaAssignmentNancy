package com.swaglabs.reporting;


public interface Reporter {

    void logPass(String tcId);

    void logFail(String tcId, String reason);

    void printSummary();
}
