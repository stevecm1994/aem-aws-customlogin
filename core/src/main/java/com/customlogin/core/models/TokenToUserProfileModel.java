package com.customlogin.core.models;

import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.aws.service.AWSDynamoDBService;
import com.customlogin.core.aws.service.JWTProcessor;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

@Model(adaptables = SlingHttpServletRequest.class)
public class TokenToUserProfileModel {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenToUserProfileModel.class);
	
	@Inject
	private SlingHttpServletRequest slingHttpServletRequest;
	
	@OSGiService
	private JWTProcessor jWTProcessor;
	
	@OSGiService
	AWSConfigurations awsConfigurations;
	
	private UserDetails userDetails;
	
	private boolean isAutheticated;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		this.isAutheticated = false;
		final Cookie cookie = slingHttpServletRequest.getCookie("JwtToken");		
		if (null != cookie) {
			String jwtIDToken = cookie.getValue();
			LOGGER.info("jwt Token from init {}",jwtIDToken);
			try {
				JWTClaimsSet claims = jWTProcessor.getJwtProcessor().process(jwtIDToken, null);
				AWSDynamoDBService awsDynamoDBService = new AWSDynamoDBService(awsConfigurations);
				this.userDetails = awsDynamoDBService.getUserDetails(claims.getClaims().get("cognito:username").toString());
				this.isAutheticated = true;
				LOGGER.info("USER NAME -- > {} , EMAIL -- > {}  ",userDetails.getFirstName(),userDetails.getEmail());
			} catch (ParseException | BadJOSEException | JOSEException e) {
				LOGGER.info("***********JWT Claims cannot be proccessed for the path {} **********");
				LOGGER.info(e.toString());
			}
		}
	}
	
	public UserDetails getUserDetails() {
		return userDetails;
	}
	
	public boolean getIsAutheticated() {
		return isAutheticated;
	}
	

}
