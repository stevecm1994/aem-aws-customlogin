package com.customlogin.core.service;

import java.io.IOException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.customlogin.core.aws.service.AWSCognitoService;
import com.customlogin.core.aws.service.AWSConfigurations;
import com.google.gson.JsonObject;

public class SignUpProcessor implements RequestProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(SignUpProcessor.class);

	@Override
	public void processRequest(SlingHttpServletRequest request, SlingHttpServletResponse response,AWSConfigurations awsConfigurations) throws IOException {
		LOG.info("insdie signup method");
		AWSCognitoService congnitoService = new AWSCognitoService(awsConfigurations);
		String isSuccess = "false";
		SignUpResult signUpResult = null;
		try {
			signUpResult = congnitoService.signUp(request.getParameter("userName"), request.getParameter("email"), request.getParameter("password"));
			if(signUpResult != null) {
				isSuccess = "true";
			}
		} catch (Exception e) {			
			LOG.info(e.toString());
		}
		renderResponse(isSuccess,response);
	}

	@Override
	public void renderResponse(String status, SlingHttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		JsonObject responseObject = new JsonObject();
		if(!status.isEmpty() && status.equalsIgnoreCase("true")) {
			responseObject.addProperty("signUpStatus", "success");
		}
		else {
			responseObject.addProperty("signUpStatus", "failed");
		}		
		response.getWriter().print(responseObject.toString());	
		
	}

}
