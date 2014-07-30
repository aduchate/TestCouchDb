package org.taktik.jrebel.test.applications.app;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SpringApp extends BaseApp {
	protected ResourceLoader resourceLoader;
	protected String nodeName;
	protected String seeds;

	protected SpringApp(File homeDir, String nodeName, String seeds) {
		super(homeDir);
		this.nodeName = nodeName;
		this.seeds = seeds;

		// Setup default ResourceLoader
		resourceLoader = createResourceLoader();
	}

	protected ResourceLoader createResourceLoader() {
		return new DefaultResourceLoader() {
			@Override
			protected Resource getResourceByPath(String path) {
				return new FileSystemResource(new File(homeDir, path));
			}
		};
	}

	protected PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		propertySourcesPlaceholderConfigurer.setIgnoreResourceNotFound(false);
		propertySourcesPlaceholderConfigurer.setNullValue("null");
		propertySourcesPlaceholderConfigurer.setLocalOverride(true);
		propertySourcesPlaceholderConfigurer.setFileEncoding("UTF8");
		return propertySourcesPlaceholderConfigurer;
	}

	protected List<PropertySource<?>> getResourcePropertySources(String... locations) throws IOException {
		List<PropertySource<?>> propertySourcesList = new ArrayList<PropertySource<?>>();
		if (locations != null) {
			for (String location : locations) {
				Resource resource = resourceLoader.getResource(location);
				EncodedResource encodedResource = new EncodedResource(resource, "UTF8");
				ResourcePropertySource resourcePropertySource = new ResourcePropertySource(resource.getFilename(), encodedResource);
				propertySourcesList.add(resourcePropertySource);
			}
		}
		return propertySourcesList;
	}

	protected PropertySource<?> getBasicPropertySource() {
		Map<String, Object> properties = new HashMap<>();
		return new MapPropertySource("basic.properties", properties);
	}

	protected GenericXmlApplicationContext createContext() throws IOException {
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

		// Add icure-*.properties property sources
		List<PropertySource<?>> resourcePropertySources = getResourcePropertySources("config/icure-default.properties", "config/icure-custom.properties");
		resourcePropertySources.forEach(propertySources::addFirst);

		return context;
	}
}