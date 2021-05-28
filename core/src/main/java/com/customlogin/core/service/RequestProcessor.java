package com.customlogin.core.service;

import java.io.IOException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

public interface  RequestProcessor {
	
	public  void processRequest(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws IOException;
	public void renderResponse(final String status ,SlingHttpServletResponse response) throws IOException;


}
