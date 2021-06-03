package com.customlogin.core.service;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.aws.service.JWTProcessor;

public interface DataRequestProcessor {
	
	public  void processDataRequest(SlingHttpServletRequest request,Cookie cookie, SlingHttpServletResponse response,
			AWSConfigurations awsConfigurations ,JWTProcessor jWTProcessor) throws IOException;

}
