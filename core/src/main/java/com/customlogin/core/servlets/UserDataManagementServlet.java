package com.customlogin.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.aws.service.JWTProcessor;
import com.customlogin.core.service.DataRequestProcessor;
import com.customlogin.core.service.UpdateUserProfile;


@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=Servlet to Update User Data in DB",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/customlogin/userDataManagment",
        "sling.servlet.extensions=" + "json"
})
public class UserDataManagementServlet extends SlingAllMethodsServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDataManagementServlet.class);
	
	@Reference
	JWTProcessor jWTProcessor;
	
	@Reference
	AWSConfigurations awsConfigurations;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Do nothing
	}
	
	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
		
		final Cookie cookie = request.getCookie("JwtToken");
		final String selector = request.getRequestPathInfo().getSelectorString();
		LOGGER.info("starting update user post method 1");
		if (null != cookie) {
			DataRequestProcessor dataRequestProcessor = getRequestType(selector);			
			LOGGER.info("starting update user post method 2");
			dataRequestProcessor.processDataRequest(cookie, response, awsConfigurations, jWTProcessor);
			
		}
	}
	
	private DataRequestProcessor getRequestType(String selector) {
		
		DataRequestProcessor dataRequestProcessor = null;		
		switch (selector) {
		case "UPDATE":
			dataRequestProcessor = new UpdateUserProfile();
			break;
		case "GETUSERDETAILS":
			dataRequestProcessor = new UpdateUserProfile();
			break;
		default:
			break;
		}
		
		return dataRequestProcessor;		
	}

}
