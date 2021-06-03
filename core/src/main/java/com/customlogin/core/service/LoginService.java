package com.customlogin.core.service;

import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customlogin.core.aws.service.AWSCognitoService;
import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.models.UserLoginStatus;

public class LoginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
	
	public UserLoginStatus normalLoginAuthentication(SlingHttpServletResponse response,final String username, final String password,AWSConfigurations awsConfigurations) {
		LOGGER.info("Inside LoginServiceImpl :  normalLoginAuthentication()");
		String idToken = authenticate(username, password,awsConfigurations);
		UserLoginStatus userLoginStatus = setJwtCookie(response, idToken);
		LOGGER.info("Exiting LoginServiceImpl :  normalLoginAuthentication()");
		return userLoginStatus;
	}
	
	public String authenticate(final String userName, final String password,AWSConfigurations awsConfigurations) {

		LOGGER.debug("Entering Authenticate of UserProfile");
		String idToken = StringUtils.EMPTY;
		AWSCognitoService congnitoService = new AWSCognitoService(awsConfigurations);
		try {
			Map<String, String> authResults = congnitoService.login(userName, password);			
			if(authResults.get("message").equals("Successfully login")) {
				idToken = authResults.get("idToken");
				LOGGER.info("User = {} Successfully Authetoicated" ,userName );
				LOGGER.info("IdToken = {} " , authResults.get("idToken"));
			}
			else {				
				LOGGER.info("Authetication Failed");
			}
		} catch (Exception e) {
			LOGGER.info("AWS Cognito authetication error : {} " , e.getMessage());
		}
		return idToken;
	}
	
	public UserLoginStatus setJwtCookie(SlingHttpServletResponse response,String idToken) {
		UserLoginStatus userLoginStatus = new UserLoginStatus();
		Cookie jwtTokenCookie = null;
		try {
			if (!idToken.isEmpty()) {
				jwtTokenCookie = new Cookie("JwtToken", idToken);
				jwtTokenCookie.setPath(";Path=/;HttpOnly;");// set HttpOnly only using javax servlet 2.5
				jwtTokenCookie.setMaxAge(1800);// cookie age in seconds.
				response.addCookie(jwtTokenCookie);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while setting cookie in setSessionDetails", e);
		}
		userLoginStatus.setIsAuthecticated("true");
		userLoginStatus.setIdToken(idToken);
		return userLoginStatus;
	}

}
