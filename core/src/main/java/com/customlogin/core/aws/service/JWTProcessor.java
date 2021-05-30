package com.customlogin.core.aws.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

@Designate(ocd=JWTProcessor.Config.class)
@Component(service=JWTProcessor.class)
public class JWTProcessor {
	
	@ObjectClassDefinition(name="JWT Processing Service",
            description = "Service to Configure and Create JWT processor")
	public static @interface Config {
		
		@AttributeDefinition(name = "AWS Json Web Key Url",description = "Specify AWS Acess Key")
		String aws_cognitoIdentity_poolUrl() default StringUtils.EMPTY;
	}
	
	@Activate
    protected void activate(final Config config) {
		setJwtProcessor(config.aws_cognitoIdentity_poolUrl());	
    }
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTProcessor.class);
	
	private static final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
	
	private static final String extension = "/.well-known/jwks.json";
	

	private ConfigurableJWTProcessor jwtProcessor ;
	
	private String identityPoolUrl;
		
	private void setJwtProcessor(String cognitoIdentityPoolUrl) {		 
		try {
			if(!cognitoIdentityPoolUrl.isEmpty()) {
				ResourceRetriever resourceRetriever =
				         new DefaultResourceRetriever(2000,2000);
				this.jwtProcessor = new DefaultJWTProcessor();
				JWKSource jwkSource = new RemoteJWKSet(new URL(cognitoIdentityPoolUrl.concat(extension)),resourceRetriever);
				JWSKeySelector keySelector = new JWSVerificationKeySelector(jwsAlgorithm, jwkSource);
				this.jwtProcessor.setJWSKeySelector(keySelector);
			}			
		} catch (MalformedURLException e) {
			LOGGER.info("error in seting jwtProcessor");
		} 
	}
	public ConfigurableJWTProcessor getJwtProcessor() {
		return jwtProcessor;
	}
		
	public boolean validateIDToken(JWTClaimsSet claims) {
		boolean isIDTokenValid = false;
		if(!claims.getIssuer().equals(identityPoolUrl)) {
			isIDTokenValid= true;
			LOGGER.info(claims.getClaims().get("cognito:username").toString());
		}
		return isIDTokenValid;		
	}
	
}
