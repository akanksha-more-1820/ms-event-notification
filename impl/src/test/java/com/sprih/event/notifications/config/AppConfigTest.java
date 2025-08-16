package com.sprih.event.notifications.config;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;
public class AppConfigTest {

    @InjectMocks
    private AppConfig appConfig;

    @Test
    public void restTemplateBeanShouldBeCreated() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        Assert.assertNotNull(restTemplate);
        context.close();
    }
}