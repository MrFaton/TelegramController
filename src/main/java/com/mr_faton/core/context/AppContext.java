package com.mr_faton.core.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContext {
    private static final ApplicationContext appCtx = new ClassPathXmlApplicationContext("AppConfig.xml");

    public static final Object getBeanByName(String beanName) {
        return appCtx.getBean(beanName);
    }
}
