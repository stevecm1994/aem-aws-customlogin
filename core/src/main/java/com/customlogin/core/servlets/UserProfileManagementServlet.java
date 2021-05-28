package com.customlogin.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service=Servlet.class,
property={
        "sling.servlet.methods=" + HttpConstants.METHOD_GET + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/customlogin/userManagement",
        "sling.servlet.extensions=" + "json"
})
public class UserProfileManagementServlet extends SlingAllMethodsServlet{
	

	private static final long serialVersionUID = 7496637721636510388L;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Do nothing
	}
	
	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Do nothing
	}

}
