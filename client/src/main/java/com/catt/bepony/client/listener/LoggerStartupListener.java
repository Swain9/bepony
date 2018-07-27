package com.catt.bepony.client.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.catt.bepony.common.util.FileUtils;
import com.catt.bepony.client.start.Main;

/**
 * 加载启动日志
 * 使用监听器初始化日志参数，还有一种方式为PropertyDefiner
 *
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-24 17:21
 */
public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {
    private static final String DEFAULT_LOG_FILE = "server";

    private boolean started = false;

    @Override
    public void start() {
        if (started) {
            return;
        }
        String jarPath = (FileUtils.urlToFile(FileUtils.getLocation(Main.class))).getParentFile().getPath();
        System.setProperty("jarPath", jarPath);
        Context context = getContext();
        context.putProperty("jarPath", jarPath);
        //String userHome = System.getProperty("user.home");
        // log.file is our custom jvm parameter to change log file name dynamicly if needed
        //String logFile = System.getProperty("log.file");

//        logFile = (logFile != null && logFile.length() > 0) ? logFile : DEFAULT_LOG_FILE;

        //Context context = getContext();

//        context.putProperty("MY_HOME", userHome);
//        context.putProperty("LOG_FILE", logFile);

        started = true;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isResetResistant() {
        return true;
    }

    @Override
    public void onStart(LoggerContext context) {
    }

    @Override
    public void onReset(LoggerContext context) {
    }

    @Override
    public void onStop(LoggerContext context) {
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }
}
