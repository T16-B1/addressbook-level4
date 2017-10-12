package seedu.address.testutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import seedu.address.commons.core.LogsCenter;

/**
 * Simple class to get logging messages
 * To Use:
 *   1. Instantiate logger with class of interest
 *   2. Retrieve message after each log generating event.
 *      @see TestLogger#getTestCapturedLog()
 *   3. Logger closes automatically after retrieving one message.
 *   this logger will print all the logs in a single event without a newline.
 */
public class TestLogger {

    private OutputStream logCapturingStream;
    private StreamHandler customLogHandler;
    private Logger logger;
    private Level originalLevel;

    /**
     * @param classType is the type of class that will be logging
     * @param level : logs with level >= level will be filtered
     */
    public TestLogger(Class<?> classType, Level level) {
        logger = LogsCenter.getLogger(classType);
        attachLogCapturer(level);
        originalLevel = logger.getLevel();
        logger.setLevel(level);
    }

    /**
     * Attaches Log Capturer to the logger.
     */
    public void attachLogCapturer(Level level) {
        logCapturingStream = new ByteArrayOutputStream();
        customLogHandler = new StreamHandler(logCapturingStream, new testLogFormatter());
        customLogHandler.setLevel(level);
        logger.addHandler(customLogHandler);
    }

    /**
     * @return captured log message in format "LEVEL - MESSAGE"
     */
    public String getTestCapturedLog() throws IOException {
        customLogHandler.flush();
        close();
        return logCapturingStream.toString();
    }

    /**
     * Detaches the Log Capturer from logger
     */
    public void close() {
        detachLogCapturer();
        logger.setLevel(originalLevel);
    }

    public void detachLogCapturer() {
        logger.removeHandler(customLogHandler);
    }

    /**
     * Custom Formatter for formatting log messages in TestLogger
     */
    public class testLogFormatter extends Formatter {

        /**
         * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
         */
        @Override
        public String format(final LogRecord record) {
            MessageFormat messageFormat = new MessageFormat("{0} - {1}\n");
            return messageFormat.format(new Object[] {record.getLevel(), record.getMessage()});
        }

    }
}
