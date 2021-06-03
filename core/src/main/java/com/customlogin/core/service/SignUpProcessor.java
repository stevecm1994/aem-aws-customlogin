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

public class SignUpProcessor implements AuthenticationRequestProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(SignUpProcessor.class);

	@Override
	public void processRequest(SlingHttpServletRequest request, SlingHttpServletResponse response,AWSConfigurations awsConfigurations) throws IOException {
		LOG.info("insdie signup method");
		AWSCognitoService congnitoService = new AWSCognitoService(awsConfigurations);
		final String userName = request.getParameter("userName");
		final String email = request.getParameter("email");
		final String passWord = request.getParameter("password");
		LOG.info("username - > {} , pass -- > {} ", userName,passWord);
		String isSuccess = "false";
		SignUpResult signUpResult = null;
		try {
			signUpResult = congnitoService.signUp(userName, email, passWord);
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
