package ru.javaops.masterjava.upload;

import org.thymeleaf.TemplateEngine;
import ru.javaops.masterjava.common.web.ThymeleafUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafListener implements ServletContextListener {
    public static TemplateEngine engine;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        engine = ThymeleafUtil.getTemplateEngine(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
