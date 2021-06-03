package com.customlogin.core.aws.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.customlogin.core.models.UserDetails;

public class AWSDynamoDBService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AWSDynamoDBService.class);
	
	private AmazonDynamoDB client  ;
	private DynamoDB dynamoDb;
	private String userTable;
	
	public AWSDynamoDBService(AWSConfigurations awsConfigurations) {
		LOG.info("Inside Dynamo db Constructor");
		client = createDynamoDbClient(awsConfigurations); 
		dynamoDb = new DynamoDB(client);
		userTable = awsConfigurations.getUsertable();
	}
	
	private AmazonDynamoDB createDynamoDbClient(AWSConfigurations awsConfigurations) {
		AWSCredentials cred = new BasicAWSCredentials(awsConfigurations.getAwsAccessKey(),awsConfigurations.getAwsSecretKey());
		AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(cred);
		return AmazonDynamoDBClientBuilder.standard().withCredentials(credProvider).withRegion(Regions.US_EAST_1).build();
		
	}
	
	public String addUserDetails(UserDetails user) {
		Table table = dynamoDb.getTable("thc-user-profile-test");		
		PutItemOutcome outcome = table.putItem(new Item().withPrimaryKey("userId", user.getUserID()).with("first_name",user.getUserID())
							.with("email", user.getEmail()));
		if (Objects.nonNull(outcome))
			return "true";
		else
			return "false";
	}
	
	public UserDetails getUserDetails(String key) {
		Table table = dynamoDb.getTable(userTable);
		GetItemSpec spec = new GetItemSpec().withPrimaryKey("userId", key);
		try {
			LOG.info("attemting to ead from dynamodb");
			Item result = table.getItem(spec);
			if (Objects.nonNull(result)) {
				UserDetails user = new UserDetails();
				user.setUserID(result.get("userId").toString());
				user.setFirstName(result.get("first_name").toString());
				user.setEmail(result.get("email").toString());
				return user;
			}
		} catch (Exception e) {
			LOG.info("Exception occurred during getUserDetails : ", e);
		}
		return null;
	}

}
