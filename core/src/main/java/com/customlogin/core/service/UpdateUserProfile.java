package com.customlogin.core.service;


import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.aws.service.AWSDynamoDBService;
import com.customlogin.core.aws.service.JWTProcessor;
import com.customlogin.core.models.UserDetails;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

public class UpdateUserProfile implements DataRequestProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserProfile.class);

	@Override
	public void processDataRequest(SlingHttpServletRequest request,Cookie cookie, SlingHttpServletResponse response,
			AWSConfigurations awsConfigurations, JWTProcessor jWTProcessor) throws IOException {
		String jwtIDToken = cookie.getValue();
		String isSuccess = "false";
		try {
			LOGGER.info("Starting processDataRequest in UpdateUserProfile ");
			JWTClaimsSet claims = jWTProcessor.getJwtProcessor().process(jwtIDToken, null);			
			UserDetails userDetails = setUserProfile(request,claims);			
			AWSDynamoDBService aWSDynamoDBService = new AWSDynamoDBService(awsConfigurations);
			isSuccess = aWSDynamoDBService.updateUserProfile(userDetails);				
		} catch (ParseException |BadJOSEException | JOSEException e) {
			LOGGER.info("***********JWT Claims cannot be proccessed for the path {} **********");
			LOGGER.info(e.toString());
		}
		renderResponse(isSuccess, response);
		
	}
	
	private UserDetails setUserProfile(SlingHttpServletRequest request ,JWTClaimsSet claims) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUserID(claims.getClaims().get("cognito:username").toString());
		userDetails.setFirstName(request.getParameter("firstname").toString());
		userDetails.setLastName(request.getParameter("lastname").toString());
		userDetails.setGender(request.getParameter("gender").toString());
		userDetails.setIntrest(request.getParameter("intrest").toString());
		userDetails.setAddress(request.getParameter("address").toString());
		LOGGER.info(userDetails.toString());
		return userDetails;		
	}
	
	public void renderResponse(String status, SlingHttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		JsonObject responseObject = new JsonObject();
		if(!status.isEmpty() && status.equalsIgnoreCase("true")) {
			responseObject.addProperty("updateProfileStatus", "success");
		}
		else {
			responseObject.addProperty("updateProfileStatus", "failed");
		}		
		response.getWriter().print(responseObject.toString());	
	}

}
