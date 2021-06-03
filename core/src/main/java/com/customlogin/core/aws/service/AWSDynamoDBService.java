package com.customlogin.core.aws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
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
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
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
				user.setUserID(result.getString("userId"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setAddress(result.getString("address"));
				user.setEmail(result.getString("email"));
				user.setGender(result.getString("gender"));
				user.setIntrest(result.getString("intrest"));
				return user;
			}
		} catch (Exception e) {
			LOG.info("Exception occurred during getUserDetails : ", e);
		}
		return null;
	}
	
	public String updateUserProfile(UserDetails user) {		
		Table table = dynamoDb.getTable(userTable);
		ValueMap valueMap = new ValueMap();
		String isSuccess = "false";
		String updateExpression = getUpdateExpression(user,valueMap);
		try {			
			if(!valueMap.isEmpty()) {
				UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("userId", user.getUserID()).				
						withUpdateExpression(updateExpression).withValueMap(valueMap).withReturnValues(ReturnValue.UPDATED_NEW);
				UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
				isSuccess = "true";
			}
			
		}catch (Exception e) {
           LOG.info("Unable to update items");
            LOG.info(e.getMessage());
        }	
		return isSuccess;
	}
	
	private String  getUpdateExpression(UserDetails user ,ValueMap valueMap) {	
		
		StringBuilder sb = new StringBuilder();
		List<String> updatefields = new ArrayList<>();
		if(!StringUtils.isEmpty(user.getFirstName())) {
			updatefields.add("first_name = :fn");
			valueMap.withString(":fn", user.getFirstName());
		}
		if(!StringUtils.isEmpty(user.getLastName())) {
			updatefields.add("last_name = :ln");
			valueMap.withString(":ln", user.getLastName());
		}
		if(!StringUtils.isEmpty(user.getGender())) {
			updatefields.add("gender = :gd");
			valueMap.withString(":gd", user.getGender());
		}
		if(!StringUtils.isEmpty(user.getIntrest())) {
			updatefields.add("intrest = :it");
			valueMap.withString(":it", user.getIntrest());
		}
		if(!StringUtils.isEmpty(user.getAddress())) {
			updatefields.add("address = :ad");
			valueMap.with(":ad", user.getAddress());
		}
		sb.append("set ");
		for(int i =0;i<updatefields.size();i++) {
			sb.append(updatefields.get(i));
			if(i != (updatefields.size()-1) ) {
				sb.append(", ");
			}
		}
		return  sb.toString();		
	}

}
