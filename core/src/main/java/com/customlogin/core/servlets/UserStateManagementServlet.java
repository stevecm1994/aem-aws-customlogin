package com.customlogin.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.customlogin.core.aws.service.AWSConfigurations;
import com.customlogin.core.service.ConfirmSignUpProcessor;
import com.customlogin.core.service.LoginProcessor;
import com.customlogin.core.service.RequestProcessor;
import com.customlogin.core.service.SignUpProcessor;

@Component(service=Servlet.class,
immediate = true,
property={
        "sling.servlet.methods=" + HttpConstants.METHOD_GET + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/customlogin/userManagement",
        "sling.servlet.extensions=" + "json"
})
public class UserStateManagementServlet extends SlingAllMethodsServlet{
	

	private static final long serialVersionUID = 7496637721636510388L;
	
	@Reference
	AWSConfigurations awsConfigurations;
	

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		// Do nothing
	}
	
	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		final String selector = request.getRequestPathInfo().getSelectorString();
		RequestProcessor requestProcessor = getRequestType(selector);
		if(requestProcessor != null) {
			requestProcessor.processRequest(request, response, awsConfigurations);
		}
	}
	
	private RequestProcessor getRequestType(String selector) {		
		RequestProcessor requestProcessor = null;
		switch (selector) {
		case "LOGIN":
			requestProcessor = new LoginProcessor();
			break;
		case "SIGNUP":
			requestProcessor = new SignUpProcessor();
			break;
		case "CONFIRMUSER":
			requestProcessor = new ConfirmSignUpProcessor();
			break;
		default:
			break;
		}
		return requestProcessor;
	}

}
