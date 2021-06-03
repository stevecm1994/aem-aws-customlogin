package com.customlogin.core.models;

public class UserDetails {
	
	private String userID;
	private String email;
	private String firstName;
	private String lastName;
	private String phNumber;
	private String address;
	private String intrest;
	private String gender;
	
	public String getPhNumber() {
		return phNumber;
	}
	public void setPhNumber(String phNumber) {
		this.phNumber = phNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIntrest() {
		return intrest;
	}
	public void setIntrest(String intrest) {
		this.intrest = intrest;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return "UserDetails [userID=" + userID + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + ", phNumber=" + phNumber + ", address=" + address + ", intrest=" + intrest + ", gender="
				+ gender + "]";
	}

}
