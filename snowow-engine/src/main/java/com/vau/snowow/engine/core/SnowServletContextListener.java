package com.vau.snowow.engine.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SnowServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        SnowContext.onStarted();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
        SnowContext.onFinished();
    }
}
