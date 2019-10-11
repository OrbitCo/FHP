package com.fmcna.fhpckd.staging;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fmcna.fhpckd.beans.staging.MemberMergeStagingData;

/**
 * Member Merge RowMapper for FhpSplitMergeRequest.
 * 
 * @author vidhishanandhikonda
 *
 */
public class MemberMergeRowMapper implements RowMapper<MemberMergeStagingData> {

	@Override
	public MemberMergeStagingData mapRow(ResultSet rs, int rowNum) throws SQLException {
		MemberMergeStagingData memberMergeStagingData = new MemberMergeStagingData();

		memberMergeStagingData.setRowIdPrimary(rs.getLong("MEMBER_ROW_ID_PRIMARY"));
		memberMergeStagingData.setSfIdPrimary(rs.getString("MEMBER_PRIMARY_SALESFORCE_ID"));
		memberMergeStagingData.setMrnPrimary(rs.getString("MEMBER_PRIMARY_MRN"));

		memberMergeStagingData.setRowIdMerge(rs.getLong("MEMBER_ROW_ID_MERGE"));
		memberMergeStagingData.setSfIdMerge(rs.getString("MEMBER_MERGE_SALESFORCE_ID"));
		memberMergeStagingData.setMrnMerge(rs.getString("MEMBER_MERGE_MRN"));

		return memberMergeStagingData;
	}

}
