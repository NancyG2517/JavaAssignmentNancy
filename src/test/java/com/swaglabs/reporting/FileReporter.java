package com.swaglabs.reporting;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileReporter extends BaseReporter {

    private static final Logger logger = LoggerManager.getLogger(FileReporter.class);

    private static final String REPORT_DIR  = "test-results";
    private static final String REPORT_FILE = REPORT_DIR + "/test-results.txt";

    private static final DateTimeFormatter DATETIME_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileReporter() {
        new File(REPORT_DIR).mkdirs();
        writeHeader();
        logger.debug("DEBUG: FileReporter initialised. Output = " + REPORT_FILE);
    }

    @Override
    public void logPass(String tcId) {
        String timestamp = LocalDateTime.now().format(DATETIME_FMT);
        String line = "[" + timestamp + "] [PASS] " + tcId + " | Status: PASSED";
        appendToFile(line);
        logger.info("FileReporter: PASS written for " + tcId);
        recordPass();
    }

    @Override
    public void logFail(String tcId, String reason) {
        String timestamp = LocalDateTime.now().format(DATETIME_FMT);
        String line = "[" + timestamp + "] [FAIL] " + tcId +
                " | Status: FAILED | Reason: " + reason;
        appendToFile(line);
        logger.error("FileReporter: FAIL written for " + tcId + " — " + reason);
        recordFail();
    }

    @Override
    public void printSummary() {
        super.printSummary();   // print to console via BaseReporter

        String sep = "----------------------------------------------";
        appendToFile(sep);
        appendToFile("SUMMARY: Total=" + getTotalTests() +
                " | Passed=" + getPassedTests() +
                " | Failed=" + getFailedTests());
        appendToFile(sep);

        logger.info("FileReporter: Full results written to: " + REPORT_FILE);
        System.out.println("  [FileReporter] Results file: " + REPORT_FILE);
    }

    private void writeHeader() {
        String sep = "==============================================";
        String header =
                sep + "\n" +
                "  SwagLabs Test Results — FileReporter\n" +
                "  Generated: " + LocalDateTime.now().format(DATETIME_FMT) + "\n" +
                sep;
        writeToFile(header, false); 
    }

    private void appendToFile(String content) {
        writeToFile(content, true); 
    }

    private void writeToFile(String content, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(REPORT_FILE, append))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            logger.error("ERROR: FileReporter could not write to '" +
                    REPORT_FILE + "': " + e.getMessage());
        }
    }
}