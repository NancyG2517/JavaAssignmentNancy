package com.swaglabs.reporting;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleReporter extends BaseReporter {

    private static final Logger logger = LoggerManager.getLogger(ConsoleReporter.class);
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void logPass(String tcId) {
        String time    = LocalDateTime.now().format(TIME_FMT);
        String message = "[" + time + "] [CONSOLE-REPORTER] [PASS] >>>  " +
                tcId + " — Test PASSED successfully";

        System.out.println(message);
        logger.info(message);

        recordPass();
    }

    @Override
    public void logFail(String tcId, String reason) {
        String time    = LocalDateTime.now().format(TIME_FMT);
        String message = "[" + time + "] [CONSOLE-REPORTER] [FAIL] >>>  " +
                tcId + " — Test FAILED | Reason: " + reason;

        System.out.println(message);
        logger.error(message);

        recordFail();
    }

    @Override
    public void printSummary() {
        System.out.println("------ ConsoleReporter Results ------");
        super.printSummary();  
    }
}
