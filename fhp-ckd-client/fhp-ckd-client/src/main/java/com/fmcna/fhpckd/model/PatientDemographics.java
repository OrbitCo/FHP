/**
 * 
 */
package com.fmcna.fhpckd.model;

import java.io.Serializable;

/**
 * @author bhupeshkantamneni
 */
public class PatientDemographics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long sourceId;
	private String ssn;
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
	private String firstName;
	private String middleName;
	private String lastName;
	private String dob;
	private String gender;
	private String race;
	private String email;
	private String status;
	private String lobVendor;
	private String lobType;
	private String hicn;
	private String claimSubscriberId;
	private String mbi;
	private String medicaidNo;
	private String latestEligibilityMemberId;
	private String mrn;

	public long getSourceId() {
		return sourceId;
	}

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}

	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @return the island
	 */
	public String getIsland() {
		return island;
	}

	/**
	 * @param island the island to set
	 */
	public void setIsland(String island) {
		this.island = island;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the alternatePhone
	 */
	public String getAlternatePhone() {
		return alternatePhone;
	}

	/**
	 * @param alternatePhone the alternatePhone to set
	 */
	public void setAlternatePhone(String alternatePhone) {
		this.alternatePhone = alternatePhone;
	}

	/**
	 * @return the emergencyPhone
	 */
	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	/**
	 * @param emergencyPhone the emergencyPhone to set
	 */
	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	/**
	 * @return the eveningPhone
	 */
	public String getEveningPhone() {
		return eveningPhone;
	}

	/**
	 * @param eveningPhone the eveningPhone to set
	 */
	public void setEveningPhone(String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the race
	 */
	public String getRace() {
		return race;
	}

	/**
	 * @param race the race to set
	 */
	public void setRace(String race) {
		this.race = race;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the lobVendor
	 */
	public String getLobVendor() {
		return lobVendor;
	}

	/**
	 * @param lobVendor the lobVendor to set
	 */
	public void setLobVendor(String lobVendor) {
		this.lobVendor = lobVendor;
	}

	/**
	 * @return the lobType
	 */
	public String getLobType() {
		return lobType;
	}

	/**
	 * @param lobType the lobType to set
	 */
	public void setLobType(String lobType) {
		this.lobType = lobType;
	}

	/**
	 * @return the hicn
	 */
	public String getHicn() {
		return hicn;
	}

	/**
	 * @param hicn the hicn to set
	 */
	public void setHicn(String hicn) {
		this.hicn = hicn;
	}

	/**
	 * @return the claimSubscriberId
	 */
	public String getClaimSubscriberId() {
		return claimSubscriberId;
	}

	/**
	 * @param claimSubscriberId the claimSubscriberId to set
	 */
	public void setClaimSubscriberId(String claimSubscriberId) {
		this.claimSubscriberId = claimSubscriberId;
	}

	/**
	 * @return the mbi
	 */
	public String getMbi() {
		return mbi;
	}

	/**
	 * @param mbi the mbi to set
	 */
	public void setMbi(String mbi) {
		this.mbi = mbi;
	}

	/**
	 * @return the medicaidNo
	 */
	public String getMedicaidNo() {
		return medicaidNo;
	}

	/**
	 * @param medicaidNo the medicaidNo to set
	 */
	public void setMedicaidNo(String medicaidNo) {
		this.medicaidNo = medicaidNo;
	}

	/**
	 * @param src_latest_eligibility_row_id to set
	 */
	public String getLatestEligibilityMemberId() {
		return latestEligibilityMemberId;
	}

	public void setLatestEligibilityMemberId(String latestEligibilityMemberId) {
		this.latestEligibilityMemberId = latestEligibilityMemberId;
	}
	
	/**
	 * @param src_mrn to set
	 */
	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	@Override
	public String toString() {
		return "PatientDemographics [sourceId=" + sourceId + ", ssn=" + ssn + ", address1=" + address1 + ", address2="
				+ address2 + ", address3=" + address3 + ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", county=" + county + ", island=" + island + ", country=" + country + ", phone=" + phone
				+ ", alternatePhone=" + alternatePhone + ", emergencyPhone=" + emergencyPhone + ", eveningPhone="
				+ eveningPhone + ", fax=" + fax + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", dob=" + dob + ", gender=" + gender + ", race=" + race + ", email="
				+ email + ", status=" + status + ", lobVendor=" + lobVendor + ", lobType=" + lobType + ", hicn=" + hicn
				+ ", claimSubscriberId=" + claimSubscriberId + ", mbi=" + mbi + ", medicaidNo=" + medicaidNo
				+ ", latestEligibilityMemberId=" + latestEligibilityMemberId + ", mrn=" + mrn + "]";
	}
	
}
