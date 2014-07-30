package org.taktik.jrebel.test.applications.app;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestApp extends SpringApp implements App {
    private GenericXmlApplicationContext applicationContext;

    public TestApp(File homeDir, String nodeName, String seeds) {
        super(homeDir, nodeName, seeds);
    }

    @Override
    public void start() throws Exception {
        setupApplicationContext();
    }

    @Override
    public void stop() throws Exception {

    }

    private void setupApplicationContext() {
        log.info("Configuring ..");

        // Create application context
        applicationContext = createNodeContext();

        // Load application XML configuration files
        applicationContext.load("config/modules.xml", "config/plugins.xml", "config/hosted.xml", "config/override.xml");

        // Refresh application context
        applicationContext.refresh();

        log.info("icure configuration complete");
    }

    protected GenericXmlApplicationContext createNodeContext() {
        // Create context
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.setResourceLoader(resourceLoader);

        // Get environment and propertySources
        ConfigurableEnvironment environment = context.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        // Prepare PropertySourcesPlaceholderConfigurer
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = createPropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setEnvironment(environment);
        context.addBeanFactoryPostProcessor(propertySourcesPlaceholderConfigurer);

        // Add basic property source
        PropertySource<?> basicPropertySource = getBasicPropertySource();
        propertySources.addFirst(basicPropertySource);

        return context;
    }
}
