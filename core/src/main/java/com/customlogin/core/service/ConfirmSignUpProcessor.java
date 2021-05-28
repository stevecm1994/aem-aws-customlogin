package com.customlogin.core.service;

import java.io.IOException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.customlogin.core.aws.service.AWSCognitoService;
import com.google.gson.JsonObject;

public class ConfirmSignUpProcessor implements RequestProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfirmSignUpProcessor.class);

	@Override
	public void processRequest(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		
		LOG.info("insdie confirm signup method");
		AWSCognitoService congnitoService = new AWSCognitoService();
		String isSuccess = "false";
		ConfirmSignUpResult confirmSignUpResult = null;
		
		try {
			confirmSignUpResult = congnitoService.confirmSignUp(request.getParameter("userName"), request.getParameter("confirmationCode"));
			if(confirmSignUpResult!=null) {
				isSuccess = "true";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderResponse(isSuccess,response);
	}

	@Override
	public void renderResponse(String status, SlingHttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		JsonObject responseObject = new JsonObject();
		if(!status.isEmpty() && status.equalsIgnoreCase("true")) {
			responseObject.addProperty("confirmSignUpStatus", "success");
		}
		else {
			responseObject.addProperty("confirmSignUpStatus", "failed");
		}		
		response.getWriter().print(responseObject.getAsString());	
	}

}
