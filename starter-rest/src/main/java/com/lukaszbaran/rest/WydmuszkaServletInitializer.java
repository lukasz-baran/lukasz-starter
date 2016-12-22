package com.lukaszbaran.rest;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * @author Lukasz Baran (baranl1)
 */
public class WydmuszkaServletInitializer implements WebApplicationInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(WydmuszkaServletInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.scan("com.lukaszbaran.rest");
		servletContext.addListener(new ContextLoaderListener(context));
		servletContext.addListener(new RequestContextListener());
		addApacheCxfServlet(servletContext);
		LOG.info("onStartup done");
	}

	private void addApacheCxfServlet(ServletContext servletContext) {
		CXFServlet cxfServlet = new CXFServlet();
		ServletRegistration.Dynamic appServlet = servletContext.addServlet("CXFServlet", cxfServlet);
		appServlet.setLoadOnStartup(1);
		Set<String> mappingConflicts = appServlet.addMapping("/service/*");
		if(!CollectionUtils.isEmpty(mappingConflicts)) {
			LOG.warn("Possible mapping conflict detected when creating CXFServlet: " + mappingConflicts);
		}
	}

}
