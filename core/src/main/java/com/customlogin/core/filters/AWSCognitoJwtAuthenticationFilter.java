package com.customlogin.core.filters;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customlogin.core.aws.service.JWTProcessor;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

@Component(service = Filter.class,
immediate = true,
property = {
        Constants.SERVICE_DESCRIPTION + "=Filter to check jwt Authetication to the User Profile Pages",
        EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
        EngineConstants.SLING_FILTER_PATTERN + "=/content/brand/en/profile(.*)",
        Constants.SERVICE_RANKING + ":Integer=-2000"

})
public class AWSCognitoJwtAuthenticationFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AWSCognitoJwtAuthenticationFilter.class);
	
	@Reference
	JWTProcessor jWTProcessor;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Do Nothing		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOGGER.info("inside AWSCognitoJwtAuthenticationFilter");
		final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
		Cookie cookie = slingRequest.getCookie("JwtToken");
		String jwtIDToken ;
		boolean idTokenVerified = false;
		if (null != cookie) {
			jwtIDToken = cookie.getValue();
			LOGGER.info("*****JWT Token ---> {} *******",jwtIDToken);
			try {
				JWTClaimsSet claims = jWTProcessor.getJwtProcessor().process(jwtIDToken, null);
				idTokenVerified = jWTProcessor.validateIDToken(claims);
				
			} catch (ParseException |BadJOSEException | JOSEException e) {
				LOGGER.info("***********JWT Claims cannot be proccessed for the path {} **********",slingRequest.getPathInfo());
				LOGGER.info(e.toString());
				slingResponse.sendRedirect("/content/brand/en.html");
			}			
		}else {
			LOGGER.info("***********No JWT Cookie for the path {} **********",slingRequest.getPathInfo());
			slingResponse.sendRedirect("/content/brand/en.html");
		}
		if(idTokenVerified) {
			chain.doFilter(request, response);
		}else {
			LOGGER.info("***********JWT Claim not verified {} **********",slingRequest.getPathInfo());
			slingResponse.sendRedirect("/content/brand/en.html");
		}		
	}

	@Override
	public void destroy() {
		// Do Nothing
		
	}

}
