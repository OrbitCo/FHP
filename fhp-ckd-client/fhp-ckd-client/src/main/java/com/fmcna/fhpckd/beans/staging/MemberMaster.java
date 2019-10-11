package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author manishtiwari
 *
 * This is a Java bean class that represents the data read from the Staging table for Member Master
 */

public class MemberMaster {

	private Long id;
	private String SSN;
	private String firstName;
	private String middleName;
	private String lastName;
	private String dateOfBirth;
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String state;
	private String zip;
	private String county;
	private String island;
	private String country;
	private String phone;
	private String alternatePhone;
	private String emergencyPhone;
	private String eveningPhone;
	private String fax;
	private String gender;
	private String race;
	private String email;
	private String status;
	private String claimSubscriberID;
	private String medicaidNo;
	private String lobVendor;
    private String lobType;
    private String hicn;
    private String mbi;
    private String latestEligibilityMemberId;
    private Long MRN;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSSN() {
		return SSN;
	}

	public void setSSN(String sSN) {
		SSN = sSN;
	}

	public String getClaimSubscriberID() {
		return claimSubscriberID;
	}

	public void setClaimSubscriberID(String claimSubscriberID) {
		this.claimSubscriberID = claimSubscriberID;
	}

	public String getMedicaidNo() {
		return medicaidNo;
	}

	public void setMedicaidNo(String medicaidNo) {
		this.medicaidNo = medicaidNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getIsland() {
		return island;
	}

	public void setIsland(String island) {
		this.island = island;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAlternatePhone() {
		return alternatePhone;
	}

	public void setAlternatePhone(String alternatePhone) {
		this.alternatePhone = alternatePhone;
	}

	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	public String getEveningPhone() {
		return eveningPhone;
	}

	public void setEveningPhone(String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLobVendor() {
		return lobVendor;
	}

	public void setLobVendor(String lobVendor) {
		this.lobVendor = lobVendor;
	}

	public String getLobType() {
		return lobType;
	}

	public void setLobType(String lobType) {
		this.lobType = lobType;
	}

	public String getHicn() {
		return hicn;
	}

	public void setHicn(String hicn) {
		this.hicn = hicn;
	}

	public String getMbi() {
		return mbi;
	}

	public void setMbi(String mbi) {
		this.mbi = mbi;
	}

	public String getLatestEligibilityMemberId() {
		return latestEligibilityMemberId;
	}

	public void setLatestEligibilityMemberId(String latestEligibilityMemberId) {
		this.latestEligibilityMemberId = latestEligibilityMemberId;
	}

	public Long getMRN() {
		return MRN;
	}

	public void setMRN(Long mRN) {
		MRN = mRN;
	}

}
