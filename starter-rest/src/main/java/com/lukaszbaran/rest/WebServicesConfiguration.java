package com.lukaszbaran.rest;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.google.common.collect.Lists;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

@Configuration
@ImportResource("classpath:META-INF/cxf/cxf.xml")
public class WebServicesConfiguration {

	@Autowired
	private ApplicationContext ctx;

	@Bean
	public Wydmuszka wydmuszka() {
		return new Wydmuszka();
	}

	@Bean
	public ApiListingResourceJSON apiListingResourceJSON() {
		return new ApiListingResourceJSON();
	}

	@Bean
	public ResourceListingProvider resourceListingProvider() {
		return new ResourceListingProvider();
	}

	@Bean
	public ApiDeclarationProvider apiDeclarationProvider() {
		return new ApiDeclarationProvider();
	}

	@Bean
	public BeanConfig swaggerConfig() {
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setResourcePackage("com.lukaszbaran.rest");
		beanConfig.setVersion("8");
		beanConfig.setTitle("Wydmuszka");
		beanConfig.setDescription("A highly configurable project management tool.");
		beanConfig.setScan(true);
		return beanConfig;
	}

	@Bean
	public JAXRSServerFactoryBean jaxRSServer(
			Wydmuszka wydmuszka,
			ApiDeclarationProvider apiDeclarationProvider,
			ResourceListingProvider resourceListingProvider,
			ApiListingResourceJSON apiListingResourceJSON) {
		JAXRSServerFactoryBean bean = new JAXRSServerFactoryBean();
		bean.setBus(ctx.getBean(SpringBus.class));
		bean.setAddress("/");

		bean.setServiceBeans(Lists.newArrayList(wydmuszka, apiListingResourceJSON));
		bean.setProviders(Lists.newArrayList(resourceListingProvider, apiDeclarationProvider));
		bean.create();
		return bean;
	}

}
