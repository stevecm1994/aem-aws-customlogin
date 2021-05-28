package com.customlogin.core.service;

import java.io.IOException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customlogin.core.models.UserLoginStatus;
import com.google.gson.JsonObject;

public class LoginProcessor implements RequestProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(LoginProcessor.class);
	
	@Override
	public void processRequest(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		LOG.info("Inside LoginProcessor : processRequest()");
		UserLoginStatus userLoginStatus = null;
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		userLoginStatus = new LoginService().normalLoginAuthentication(response, username, password);
		renderResponse(userLoginStatus.getIsAuthecticated(), response);
	}

	@Override
	public void renderResponse(final String status, SlingHttpServletResponse response) throws IOException {		
		response.setContentType("application/json");
		JsonObject responseObject = new JsonObject();
		if(!status.isEmpty() && status.equalsIgnoreCase("true")) {
			responseObject.addProperty("loginstatus", "success");
		}
		else {
			responseObject.addProperty("loginstatus", "failed");
		}		
		response.getWriter().print(responseObject.getAsString());			
	}


}
