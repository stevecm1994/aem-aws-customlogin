package com.customlogin.core.models;

public class UserLoginStatus {
	
    private String isAuthenticated;
	
	private String idToken;
	
	public UserLoginStatus() {
			//Constructor left blank to avoid unnecessary initialization
	}
		
	public String getIsAuthecticated() {
		return isAuthenticated;
	}

	public void setIsAuthecticated(String isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}	
	
}
