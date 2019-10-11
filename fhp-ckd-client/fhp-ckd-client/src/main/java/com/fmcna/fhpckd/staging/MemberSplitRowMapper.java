package com.fmcna.fhpckd.staging;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fmcna.fhpckd.beans.staging.MemberSplitStagingData;

/**
 * Member Split RowMapper for FhpSplitMergeRequest.
 * 
 * @author vidhishanandhikonda
 *
 */
public class MemberSplitRowMapper implements RowMapper<MemberSplitStagingData> {

	@Override
	public MemberSplitStagingData mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberSplitStagingData memberSplitStagingData = new MemberSplitStagingData();

		memberSplitStagingData.setRowIdOriginal(rs.getLong("MEMBER_ROW_ID_ORIGINAL"));
		memberSplitStagingData.setSfIdOriginal(rs.getString("MEMBER_ORIGINAL_SALESFORCE_ID"));
		memberSplitStagingData.setMrnOriginal(rs.getString("MEMBER_ORIGINAL_MRN"));
 
		memberSplitStagingData.setRowIdNew(rs.getLong("MEMBER_ROW_ID_NEW"));
		memberSplitStagingData.setSfIdNew(rs.getString("MEMBER_NEW_SALESFORCE_ID"));
		memberSplitStagingData.setMrnNew(rs.getString("MEMBER_NEW_MRN"));

		return memberSplitStagingData;
	}

}
