package com.nvivo.twitter.svc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DiegoT on 13/05/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/ApplicationContext.xml"})
public class ContextLoadTest implements ApplicationContextAware {
    private ApplicationContext appContext;

    @Test
    public void contextStartup() {
       Assert.assertNotNull(appContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.appContext = applicationContext;
    }
}
