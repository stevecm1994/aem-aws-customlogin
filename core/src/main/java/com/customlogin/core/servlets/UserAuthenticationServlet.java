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
import com.customlogin.core.service.LogoutProcessor;
import com.customlogin.core.service.AuthenticationRequestProcessor;
import com.customlogin.core.service.SignUpProcessor;

@Component(service=Servlet.class,
immediate = true,
property={
        "sling.servlet.methods=" + HttpConstants.METHOD_GET + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/customlogin/userManagement",
        "sling.servlet.extensions=" + "json"
})
public class UserAuthenticationServlet extends SlingAllMethodsServlet{
	

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
		AuthenticationRequestProcessor authrequestProcessor = getRequestType(selector);
		if(authrequestProcessor != null) {
			authrequestProcessor.processRequest(request, response, awsConfigurations);
		}
	}
	
	private AuthenticationRequestProcessor getRequestType(String selector) {		
		AuthenticationRequestProcessor authrequestProcessor = null;
		switch (selector) {
		case "LOGIN":
			authrequestProcessor = new LoginProcessor();
			break;
		case "SIGNUP":
			authrequestProcessor = new SignUpProcessor();
			break;
		case "CONFIRMUSER":
			authrequestProcessor = new ConfirmSignUpProcessor();
			break;
		case "LOGOUT":
			authrequestProcessor = new LogoutProcessor();
			break;
		default:
			break;
		}
		return authrequestProcessor;
	}

}
