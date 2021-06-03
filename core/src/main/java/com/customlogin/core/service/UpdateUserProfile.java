package com.customlogin.core.service;


import java.text.ParseException;

import javax.servlet.http.Cookie;


import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.aws.service.AWSDynamoDBService;
import com.customlogin.core.aws.service.JWTProcessor;
import com.customlogin.core.models.UserDetails;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

public class UpdateUserProfile implements DataRequestProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserProfile.class);

	@Override
	public void processDataRequest(Cookie cookie, SlingHttpServletResponse response,
			AWSConfigurations awsConfigurations, JWTProcessor jWTProcessor) {
		String jwtIDToken = cookie.getValue();
		try {
			LOGGER.info("starting update user post method 2");
			JWTClaimsSet claims = jWTProcessor.getJwtProcessor().process(jwtIDToken, null);
			UserDetails userDetails = new UserDetails();
			userDetails.setUserID(claims.getClaims().get("cognito:username").toString());
			AWSDynamoDBService aWSDynamoDBService = new AWSDynamoDBService(awsConfigurations);
			aWSDynamoDBService.addUserDetails(userDetails);				
		} catch (ParseException |BadJOSEException | JOSEException e) {
			LOGGER.info("***********JWT Claims cannot be proccessed for the path {} **********");
			LOGGER.info(e.toString());
		}
		
	}

}
