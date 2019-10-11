package com.fmcna.fhpckd.staging;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fmcna.fhpckd.beans.staging.MemberLabs;

public class MemberLabsRowMapper implements RowMapper<MemberLabs> {
    @Override
    public MemberLabs mapRow(ResultSet rs, int rowNum) throws SQLException {
    	MemberLabs memberLabs = new MemberLabs();
 
    	memberLabs.setId(rs.getLong("SRC_MEMBER_LABS_ROW_ID"));
    	memberLabs.setMemberId(rs.getLong("SRC_MEMBER_ROW_ID"));
    	memberLabs.setLabType(rs.getString("SRC_LAB_TYPE"));
    	memberLabs.setLabResult(rs.getBigDecimal("SRC_LAB_RESULT"));
    	memberLabs.setLabLoincCode(rs.getString("SRC_LAB_LOINC_CODE"));
    	memberLabs.setServiceDate(rs.getString("SRC_LAB_SERVICE_DATE"));
    	memberLabs.setRowDeleted(rs.getString("SRC_ROW_DELETED"));
        
        return memberLabs;
    }
}
