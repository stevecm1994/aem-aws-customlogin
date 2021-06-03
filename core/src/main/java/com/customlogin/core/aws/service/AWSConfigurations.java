package com.customlogin.core.aws.service;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd=AWSConfigurations.Config.class)
@Component(service=AWSConfigurations.class)
public class AWSConfigurations {
	
	@ObjectClassDefinition(name="AWS server Configurations",
            description = "Service for configuring AWS server configurations")
	public static @interface Config {
		
		@AttributeDefinition(name = "AWS Acess Key",description = "Specify AWS Acess Key")
		String aws_access_key() default StringUtils.EMPTY;
		
		@AttributeDefinition(name = "AWS Secret Key",description = "Specify AWS Secret Key")
		String aws_secret_key() default StringUtils.EMPTY;
		
		@AttributeDefinition(name = "AWS Cognito User Pool Id",description = "AWS Cognito User Pool Id")
		String user_pool_id() default StringUtils.EMPTY;
		
		@AttributeDefinition(name = "AWS Cognito Client Id",description = "AWS Cognito Client Id")
		String cognito_client_id() default StringUtils.EMPTY;
		
		@AttributeDefinition(name = "AWS DynamoDb User Table",description = "AWS DynamoDb User Table Name")
		String dynamo_db_user_table() default StringUtils.EMPTY;
		
	}
	
	private String awsAccessKey ;
	
	private String awsSecretKey;
	
	private String userPoolId;
	
	private String cognitoClientId;
	
	private String usertable;
	
	@Activate
    protected void activate(final Config config) {
		awsAccessKey = config.aws_access_key();
		awsSecretKey = config.aws_secret_key();
		userPoolId = config.user_pool_id();
		cognitoClientId = config.cognito_client_id();
		usertable = config.dynamo_db_user_table();
		
    }

	public String getUsertable() {
		return usertable;
	}

	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	public String getUserPoolId() {
		return userPoolId;
	}

	public String getCognitoClientId() {
		return cognitoClientId;
	}

}
