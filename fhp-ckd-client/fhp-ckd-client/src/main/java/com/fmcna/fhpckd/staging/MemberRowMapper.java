package com.fmcna.fhpckd.staging;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fmcna.fhpckd.beans.staging.MemberMaster;

public class MemberRowMapper implements RowMapper<MemberMaster> {
    @Override
    public MemberMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemberMaster member = new MemberMaster();
 
        member.setClaimSubscriberID(rs.getString("SRC_CLAIM_SUBSCRIBER_ID"));
        member.setDateOfBirth(rs.getString("SRC_DATE_OF_BIRTH"));
        member.setFirstName(rs.getString("SRC_FIRST_NAME"));
        member.setId(rs.getLong("SRC_MEMBER_ROW_ID"));
        member.setLastName(rs.getString("SRC_LAST_NAME"));
        member.setMedicaidNo(rs.getString("SRC_MEDICAID_NO"));
        member.setMiddleName(rs.getString("SRC_MIDDLE_NAME"));
        member.setSSN(rs.getString("SRC_SSN"));
        
        member.setAddress1(rs.getString("SRC_ADDRESS_1"));
        member.setAddress2(rs.getString("SRC_ADDRESS_2"));
        member.setAddress3(rs.getString("SRC_ADDRESS_3"));
        member.setCity(rs.getString("SRC_CITY"));
        member.setState(rs.getString("PARSED_STATE_CODE"));
        member.setZip(rs.getString("SRC_ZIP"));
        member.setCounty(rs.getString("SRC_COUNTY"));
        member.setIsland(rs.getString("SRC_ISLAND"));
        member.setCountry(rs.getString("SRC_COUNTRY"));
        member.setPhone(rs.getString("SRC_PHONE"));
        member.setAlternatePhone(rs.getString("SRC_ALTERNATE_PHONE"));
        member.setEveningPhone(rs.getString("SRC_EVENING_PHONE"));
        member.setEmergencyPhone(rs.getString("SRC_EMERGENCY_PHONE"));
        member.setFax(rs.getString("SRC_FAX"));
        member.setEmail(rs.getString("SRC_EMAIL"));
        member.setRace(rs.getString("SRC_RACE"));
        member.setGender(rs.getString("SRC_GENDER"));
        
        member.setStatus(rs.getString("SRC_STATUS"));
        member.setLobVendor(rs.getString("SRC_LOB_VENDOR"));
        member.setLobType(rs.getString("SRC_LOB_TYPE"));
        member.setHicn(rs.getString("SRC_HICN"));
        member.setMbi(rs.getString("SRC_MBI"));
        member.setLatestEligibilityMemberId(rs.getString("SRC_LATEST_ELIGIBILITY_ROW_ID"));

        member.setMRN(rs.getLong("SRC_MRN"));

        return member;
    }
}
