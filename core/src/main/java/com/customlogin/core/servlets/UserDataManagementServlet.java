package com.customlogin.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=Servlet to Update User Data in DB",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET + HttpConstants.METHOD_POST,
        "sling.servlet.resourceTypes="+ "/bin/customlogin/userDataManagment",
        "sling.servlet.extensions=" + "json"
})
public class UserDataManagementServlet extends SlingAllMethodsServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Do nothing
	}
	
	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
		
	}

}
