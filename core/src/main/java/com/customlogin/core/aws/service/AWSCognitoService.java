package com.customlogin.core.aws.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;

public class AWSCognitoService {
	
	private AWSCognitoIdentityProvider client ;
    private final String clientId = "65scll5rbd9s399dgds76dfkmj";
    private final String userPool = "us-east-1_6QziSvnhD";

    private static final Logger LOG = LoggerFactory.getLogger(AWSCognitoService.class);
    
    public AWSCognitoService() {
        client = createCognitoClient();
        //LOG.info(client.toString());
    }
    
    private AWSCognitoIdentityProvider createCognitoClient() {
    	LOG.info("inside createCognitoClient method");
        AWSCredentials cred = new BasicAWSCredentials("AKIAUWXCRC4VLYWQMDNV", "fDm6jyfL4oj8jCkZcD2rIF6O3Q4Mqyf6Ke3k5EG/");
        //AWSCredentials cred = new AnonymousAWSCredentials();
        AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(cred);
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(credProvider)
                .withRegion(Regions.US_EAST_1)
                .build();
    }
        
	public SignUpResult signUp(String userName, String email, String password) throws Exception {
		AttributeType userAttribute = new AttributeType().withName("email").withValue(email);
		SignUpRequest request = new SignUpRequest().withClientId(clientId ).withUsername(userName).withPassword(password).withUserAttributes(userAttribute);
        return client.signUp(request);
	}

	
	public ConfirmSignUpResult confirmSignUp(String userName, String confirmationCode) throws Exception {
		ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest().withClientId(clientId).withUsername(userName).withConfirmationCode(confirmationCode);
        return client.confirmSignUp(confirmSignUpRequest);
	}

	
	public Map<String, String> login(String email, String password) throws Exception {
		Map<String, String> authParams = new LinkedHashMap<String, String>() {{
            put("USERNAME", email);
            put("PASSWORD", password);
        }};

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .withUserPoolId(userPool)
                .withClientId(clientId)
                .withAuthParameters(authParams);
        AdminInitiateAuthResult authResult = client.adminInitiateAuth(authRequest);
        AuthenticationResultType resultType = authResult.getAuthenticationResult();
         return new LinkedHashMap<String, String>() {{
            put("idToken", resultType.getIdToken());
            put("accessToken", resultType.getAccessToken());
            put("refreshToken", resultType.getRefreshToken());
            put("message", "Successfully login");
        }};

	}

}
