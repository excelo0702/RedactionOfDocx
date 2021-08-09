package com.mkyong.poi.word;

public class MetaData {
	private String candidateId;
	private String city;
	private String formattedNumber;
	private String candidateGuid;
	private String emailId;
	private String location;
	private String mobile;
	public String getFormattedNumber() {
		return formattedNumber;
	}
	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}
	private String phone;
	private String lastName;
	private String FirstName;
	private String resume;
	
	
	public MetaData(String candidateId, String city, String formattedNumber, String candidateGuid, String emailId,
			String location, String mobile, String phone, String lastName, String firstName, String resume) {
		super();
		this.candidateId = candidateId;
		this.city = city;
		this.formattedNumber = formattedNumber;
		this.candidateGuid = candidateGuid;
		this.emailId = emailId;
		this.location = location;
		this.mobile = mobile;
		this.phone = phone;
		this.lastName = lastName;
		FirstName = firstName;
		this.resume = resume;
	}
	public String getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCandidateGuid() {
		return candidateGuid;
	}
	public void setCandidateGuid(String candidateGuid) {
		this.candidateGuid = candidateGuid;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	
	
	
	
}
