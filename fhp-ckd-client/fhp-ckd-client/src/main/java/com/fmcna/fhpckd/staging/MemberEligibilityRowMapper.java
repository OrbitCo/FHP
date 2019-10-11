package com.fmcna.fhpckd.staging;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fmcna.fhpckd.beans.staging.MemberEligibility;

public class MemberEligibilityRowMapper implements RowMapper<MemberEligibility> {
    @Override
    public MemberEligibility mapRow(ResultSet rs, int rowNum) throws SQLException {
    	MemberEligibility memberEligibility = new MemberEligibility();
    	
        memberEligibility.setId(rs.getLong("SRC_MEMBER_ELIGIBILITY_ROW_ID"));
        memberEligibility.setSrcMemberRowId(rs.getLong("SRC_MEMBER_ROW_ID"));
        memberEligibility.setStartDate(rs.getString("SRC_START_DATE"));
        memberEligibility.setTermDate(rs.getString("SRC_TERM_DATE"));
        memberEligibility.setTermReason(rs.getString("SRC_TERM_REASON"));
        memberEligibility.setStatus(rs.getString("SRC_STATUS"));
        memberEligibility.setLobType(rs.getString("SRC_LOB_TYPE"));
        memberEligibility.setLobVendor(rs.getString("SRC_LOB_VENDOR"));
        memberEligibility.setRowDeleted(rs.getString("SRC_ROW_DELETED"));
        memberEligibility.setHclob(rs.getString("SRC_HC_LOB"));
        memberEligibility.setHcGroup(rs.getString("SRC_HC_GROUP"));
        memberEligibility.setHcProgram(rs.getString("SRC_HC_PROGRAM"));

        return memberEligibility;
    }
}
