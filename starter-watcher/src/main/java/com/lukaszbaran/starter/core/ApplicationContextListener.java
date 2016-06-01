package com.lukaszbaran.starter.core;

import com.lukaszbaran.starter.watcher.DirectoryWatcher;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

public class ApplicationContextListener extends ContextLoaderListener {

    private static final Logger LOGGER = Logger.getLogger(ApplicationContextListener.class);


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.debug("contextInitialized()");
        super.contextInitialized(servletContextEvent);
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());

        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        DirectoryWatcher watcher = (DirectoryWatcher) applicationContext.getBean("watcher");
        executor.submit(watcher);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.debug("contextDestroyed() - start");

        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());

        DirectoryWatcher watcher = (DirectoryWatcher) applicationContext.getBean("watcher");
        watcher.setRunning(false);

        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");
        executor.shutdown();

//        Thread thread = (Thread) applicationContext.getBean("pollThread");
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            LOGGER.error("Failed to join thread", e);
//        }
//        thread.interrupt();
        super.contextDestroyed(servletContextEvent);
        LOGGER.debug("contextDestroyed() - end");
    }


}
