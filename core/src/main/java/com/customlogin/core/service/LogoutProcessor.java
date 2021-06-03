package com.customlogin.core.service;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customlogin.core.aws.service.AWSConfigurations;
import com.google.gson.JsonObject;

public class LogoutProcessor implements AuthenticationRequestProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(LogoutProcessor.class);

	@Override
	public void processRequest(SlingHttpServletRequest request, SlingHttpServletResponse response,
			AWSConfigurations awsConfigurations) throws IOException {
		LOG.info("Inside LogoutProcessor ");
		Cookie jwtTokenCookie = new Cookie("JwtToken", "");
		jwtTokenCookie.setMaxAge(0);
		jwtTokenCookie.setPath("/");
		response.addCookie(jwtTokenCookie);	
		renderResponse("true",response);
		LOG.info("Cookie Cleared");
	}

	@Override
	public void renderResponse(String status, SlingHttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		JsonObject responseObject = new JsonObject();
		if(!status.isEmpty() && status.equalsIgnoreCase("true")) {
			responseObject.addProperty("logoutStatus", "success");
		}
		else {
			responseObject.addProperty("logoutStatus", "failed");
		}		
		response.getWriter().print(responseObject.toString());			
	}

}
