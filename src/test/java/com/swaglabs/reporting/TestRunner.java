package com.swaglabs.reporting;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class TestRunner {

    private static final Logger logger = LoggerManager.getLogger(TestRunner.class);

    private static final Object[][] SIMULATED_RESULTS = {
        { "TC001", true,  null                                      },
        { "TC002", true,  null                                      },
        { "TC003", true,  null                                      },
        { "TC004", false, "Logout did not redirect to login page"   },
    };

    public static void main(String[] args) {
        logger.info("==============================================");
        logger.info("  TestRunner — Assignment 8: Polymorphism Demo");
        logger.info("==============================================");
        List<Reporter> reporters = Arrays.asList(
            new ConsoleReporter(),   
            new FileReporter(),     
            new HtmlReporter()      
        );

        for (Reporter reporter : reporters) {
            String reporterName = reporter.getClass().getSimpleName();
            logger.info("--- Processing reporter: " + reporterName + " ---");
            System.out.println("\n>>> Running: " + reporterName);

            for (Object[] row : SIMULATED_RESULTS) {
                String  tcId   = (String)  row[0];
                boolean passed = (boolean) row[1];
                String  reason = (String)  row[2];

                if (passed) {
                    reporter.logPass(tcId);           
                } else {
                    reporter.logFail(tcId, reason);  
                }
            }

            reporter.printSummary();   
        }

        logger.info("TestRunner completed. Check test-results/ folder for output files.");
    }
}
