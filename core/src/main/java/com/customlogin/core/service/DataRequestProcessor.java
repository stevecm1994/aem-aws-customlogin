package com.customlogin.core.service;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletResponse;

import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.aws.service.JWTProcessor;

public interface DataRequestProcessor {
	
	public  void processDataRequest(Cookie cookie, SlingHttpServletResponse response,AWSConfigurations awsConfigurations ,JWTProcessor jWTProcessor);

}
