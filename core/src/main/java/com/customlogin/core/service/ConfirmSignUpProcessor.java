package com.customlogin.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.customlogin.core.aws.service.AWSCognitoService;
import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.aws.service.AWSDynamoDBService;
import com.customlogin.core.models.UserDetails;
import com.google.gson.JsonObject;

public class ConfirmSignUpProcessor implements AuthenticationRequestProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfirmSignUpProcessor.class);

	@Override
	public void processRequest(SlingHttpServletRequest request, SlingHttpServletResponse response,AWSConfigurations awsConfigurations) throws IOException {
		
		LOG.info("insdie confirm signup method");
		String isSuccess = "false";
		final String userName = request.getParameter("userName");
		final String confirmationCode =request.getParameter("confirmationCode"); 
		AWSCognitoService congnitoService = new AWSCognitoService(awsConfigurations);		
		ConfirmSignUpResult confirmSignUpResult = null;		
		try {
			confirmSignUpResult = congnitoService.confirmSignUp(userName, confirmationCode);
			if(confirmSignUpResult!=null) {
				AdminGetUserResult userResult = congnitoService.getUser(userName);
				isSuccess = addUser(userResult, userName, awsConfigurations);
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
			responseObject.addProperty("confirmSignUpStatus", "success");
		}
		else {
			responseObject.addProperty("confirmSignUpStatus", "failed");
		}		
		response.getWriter().print(responseObject.toString());	
	}
	
	private String addUser(final AdminGetUserResult userResult,final String userId,AWSConfigurations awsConfigurations) throws Exception {
		String addUserSuccess = "false";
		List<AttributeType> userAttributes = userResult.getUserAttributes();
		UserDetails user = new UserDetails();
		user.setUserID(userId);
		for(AttributeType userAttribute : userAttributes) {
			if(userAttribute.getName().equalsIgnoreCase("email")) {
				user.setEmail(userAttribute.getValue());
			}
		}
		if(!user.getUserID().isEmpty() && !user.getEmail().isEmpty() ) {
			LOG.info("****User Profile Set for userID : {} and eamil : {} ****", user.getUserID(),user.getEmail());
			AWSDynamoDBService awsDynamoDBService = new AWSDynamoDBService(awsConfigurations);
			addUserSuccess = awsDynamoDBService.addUserDetails(user);
			LOG.info("***** User Adding Status -->  {} ***",addUserSuccess);
		}
		return addUserSuccess;		
	}
}
