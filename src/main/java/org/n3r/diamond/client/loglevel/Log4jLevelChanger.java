package org.n3r.diamond.client.loglevel;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Enumeration;

@SuppressWarnings("unchecked")
public class Log4jLevelChanger implements LoggerLevelChangable {
    private Level transToLog4j(LoggerLevel loggerLevel) {
        switch (loggerLevel) {
            case DEBUG:
                return Level.DEBUG;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARN;
            case ERROR:
                return Level.ERROR;
        }

        throw new RuntimeException("should not reach here");
    }

    @Override
    public void changeAll(LoggerLevel loggerLevel) {
        Level newLevel = transToLog4j(loggerLevel);
        Enumeration<Logger> currentLoggers = LogManager.getCurrentLoggers();

        while (currentLoggers.hasMoreElements()) {
            changeToNewLevel(newLevel, currentLoggers.nextElement());
        }

        changeToNewLevel(newLevel, LogManager.getRootLogger());
    }

    @Override
    public void change(String loggerName, LoggerLevel loggerLevel) {
        Level newLevel = transToLog4j(loggerLevel);
        Logger logger = Logger.getLogger(loggerName);

        changeToNewLevel(newLevel, logger);
    }

    @Override
    public void changeSome(String loggerPrefix, LoggerLevel loggerLevel) {
        Level newLevel = transToLog4j(loggerLevel);
        Enumeration<Logger> currentLoggers = LogManager.getCurrentLoggers();

        while (currentLoggers.hasMoreElements()) {
            Logger logger = currentLoggers.nextElement();
            if (logger.getName().startsWith(loggerPrefix))
                changeToNewLevel(newLevel, logger);
        }

        Logger logger = LogManager.getRootLogger();
        if (logger.getName().startsWith(loggerPrefix))
            changeToNewLevel(newLevel, logger);
    }

    private void changeToNewLevel(Level newLevel, Logger logger) {
        if (logger.getLevel() == newLevel) return;
        logger.setLevel(newLevel);
    }
}