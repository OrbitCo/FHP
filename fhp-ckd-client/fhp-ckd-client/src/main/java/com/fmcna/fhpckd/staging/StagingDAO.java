package com.fmcna.fhpckd.staging;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.staging.MemberEligibility;
import com.fmcna.fhpckd.beans.staging.MemberLabs;
import com.fmcna.fhpckd.beans.staging.MemberLoadAudit;
import com.fmcna.fhpckd.beans.staging.MemberMaster;
import com.fmcna.fhpckd.beans.staging.MemberMergeStagingData;
import com.fmcna.fhpckd.beans.staging.MemberResponse;
import com.fmcna.fhpckd.beans.staging.MemberSplitMergeResponse;
import com.fmcna.fhpckd.beans.staging.MemberSplitStagingData;
import com.fmcna.fhpckd.beans.staging.MemberStagingData;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.util.DateUtil;

@Component
public class StagingDAO {

	private static final Logger logger = LoggerFactory.getLogger(StagingDAO.class);
	
	private final String sqlMemberQuery ="SELECT SRC_MEMBER_ROW_ID, SRC_SSN, SRC_CLAIM_SUBSCRIBER_ID, SRC_MEDICAID_NO, "
			+ "SRC_FIRST_NAME, SRC_MIDDLE_NAME, SRC_LAST_NAME, SRC_DATE_OF_BIRTH, MOST_RECENT_RECORD_IND, SRC_ADDRESS_1,SRC_ADDRESS_2,SRC_ADDRESS_3,SRC_CITY, "
			+ "PARSED_STATE_CODE, SRC_ZIP,SRC_COUNTY,SRC_ISLAND,SRC_COUNTRY,SRC_PHONE,SRC_ALTERNATE_PHONE,SRC_EVENING_PHONE,SRC_EMERGENCY_PHONE,SRC_FAX,SRC_EMAIL,SRC_GENDER, "
			+ "SRC_RACE, SRC_STATUS,SRC_LOB_VENDOR,SRC_LOB_TYPE,SRC_HICN,SRC_MBI, SRC_LATEST_ELIGIBILITY_ROW_ID, SRC_MRN "
			+ "FROM FHP.FHP_MEMBER_MASTER "
			+ "WHERE SRC_MEMBER_ROW_ID = ? ";
	
	private final String sqlMemberEligibilityQuery ="SELECT SRC_MEMBER_ELIGIBILITY_ROW_ID, SRC_MEMBER_ROW_ID, SRC_ROW_DELETED, SRC_START_DATE, SRC_TERM_DATE, SRC_TERM_REASON, SRC_STATUS, "
			+ "SRC_LOB_VENDOR, SRC_LOB_TYPE,SRC_HC_LOB, SRC_HC_GROUP, SRC_HC_PROGRAM " 
			+ "FROM FHP.FHP_MEMBER_ELIGIBILITY "
			+ "WHERE SRC_MEMBER_ROW_ID = ?";
	
	private final String sqlMemberLabsQuery_bkup="SELECT SRC_MEMBER_LABS_ROW_ID, SRC_MEMBER_ROW_ID, SRC_ROW_DELETED, SRC_LAB_GFR, SRC_LAB_CREATININE, SRC_LAB_ALBUMIN, SRC_LAB_PROTEINURIA, "
			+ "SRC_LAB_PHOSPHORUS, SRC_LAB_BICARBONATE, SRC_LAB_CALCIUM, SRC_LAB_URINE, SRC_LAB_POTASSIUM, SRC_LAB_WHITE_BLOOD_CELL, SRC_LAB_HEMO_A1C, "
			+ "SRC_LAB_HEMATOCRIT, SRC_LAB_SERVICE_DATE "
			+ "FROM FHP.FHP_MEMBER_LABS "
			+ "WHERE SRC_MEMBER_ROW_ID = ?";
	
	private final String sqlMemberLabsQuery="SELECT SRC_MEMBER_LABS_ROW_ID, SRC_MEMBER_ROW_ID, SRC_ROW_DELETED, SRC_LAB_TYPE, SRC_LAB_RESULT, SRC_LAB_LOINC_CODE, SRC_LAB_SERVICE_DATE "
			+ "FROM FHP.FHP_MEMBER_LABS "
			+ "WHERE SRC_MEMBER_ROW_ID = ? AND SRC_ROW_UPDATE_DATE > ?";

	private final String sqlMemberLabsQueryAll="SELECT SRC_MEMBER_LABS_ROW_ID, SRC_MEMBER_ROW_ID, SRC_ROW_DELETED, SRC_LAB_TYPE, SRC_LAB_RESULT, SRC_LAB_LOINC_CODE, SRC_LAB_SERVICE_DATE "
			+ "FROM FHP.FHP_MEMBER_LABS "
			+ "WHERE SRC_MEMBER_ROW_ID = ? ";

	private final String sqlMemberStatusUpdateQuery ="UPDATE FHP.FHP_MEMBER_MASTER "
			+ "SET SALESFORCE_ID = ?, MEDICAL_RECORD_NUMBER = ?, EU_ID = ?, SFHC_LOAD_FLAG = ?, SFHC_LOAD_REASON = ?, SFHC_LOAD_DTTM = ? "
			+ "WHERE SRC_MEMBER_ROW_ID = ?";
	
	private final String sqlMemberLoadAuditInsertQuery ="INSERT INTO FHP.FHP_MEMBER_LOAD_AUDIT "
			+ "(SFHC_LOAD_DTTM, SRC_MEMBER_ID, OPERATION, FAILURE_COMPONENT, "
			+ "SFHC_LOAD_STATUS, SFHC_LOAD_COMMENT, TGT_MEMBER_ID, SALESFORCE_ID_MATCH1, SALESFORCE_ID_MATCH2, SALESFORCE_ID_MATCH3, SALESFORCE_ID_MATCH4, "
			+ "SALESFORCE_ID_MATCH5, SALESFORCE_ID_MATCH6, SALESFORCE_ID_MATCH7, SALESFORCE_ID_MATCH8, SALESFORCE_ID_MATCH9, SALESFORCE_ID_MATCH10, "
			+ "MEMBER_TYPE, FLOW_STATE, COMMENT_DESCRIPTION, CATEGORY, HC_MRN, EMPI_MRN, EMPI_SEARCH_STRING_REQUEST, EMPI_SEARCH_STRING_RESPONSE, "
			+ "MRN_MATCH1, MRN_MATCH2, MRN_MATCH3, MRN_MATCH4, MRN_MATCH5, MRN_MATCH6, MRN_MATCH7, MRN_MATCH8, MRN_MATCH9, MRN_MATCH10, "
			+ "SALESFORCE_ID_SFHC, SALESFORCE_ID_EMPI) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final String sqlHandshakeCountQuery = "SELECT COUNT(*) FROM SFHCADM.PROCESS_HANDSHAKE WHERE PROCESS_NAME='FHP_MEMBER_LOAD'";
	
	private final String sqlHandshakeDeleteQuery = "DELETE FROM SFHCADM.PROCESS_HANDSHAKE WHERE PROCESS_NAME='FHP_MEMBER_LOAD'";
	
	private final String sqlUpdateMemberIds= "SELECT DISTINCT MEMBER_ID FROM ( SELECT A.SRC_MEMBER_ROW_ID AS MEMBER_ID, A.SRC_ROW_UPDATE_DATE AS UPDATE_DATE FROM FHP.FHP_MEMBER_MASTER A "
			+ "WHERE A.SRC_ROW_UPDATE_DATE > ? UNION SELECT B.SRC_MEMBER_ROW_ID AS MEMBER_ID, B.SRC_ROW_UPDATE_DATE AS UPDATE_DATE FROM FHP.FHP_MEMBER_ELIGIBILITY B WHERE B.SRC_ROW_UPDATE_DATE > ? "
			+ "UNION SELECT C.SRC_MEMBER_ROW_ID AS MEMBER_ID, C.SRC_ROW_UPDATE_DATE AS UPDATE_DATE FROM FHP.FHP_MEMBER_LABS C WHERE C.SRC_ROW_UPDATE_DATE > ?  ) WHERE "
			+ "MEMBER_ID IN (SELECT SRC_MEMBER_ROW_ID FROM FHP.FHP_MEMBER_MASTER X WHERE    X.SRC_ROW_DELETED = 'N' OR (X.SRC_ROW_DELETED = 'Y' AND X.SALESFORCE_ID "
			+ "IS NOT NULL AND X.SRC_MEMBER_ROW_ID IN (SELECT MEMBER_ROW_ID_MERGE FROM FHP.FHP_Member_Merge_List) ) OR (X.SRC_ROW_DELETED = 'Y' AND X.SALESFORCE_ID "
			+ "IS NOT NULL AND X.SRC_MEMBER_ROW_ID IN (SELECT MEMBER_ROW_ID_ORIGINAL FROM FHP.FHP_Member_Split_List) )) ORDER BY MEMBER_ID";
	
	private final String sqlMaxSrcUpdateDate= "SELECT MAX(MAX_SRC_UPDATE_DATE) FROM (SELECT MAX(SRC_ROW_UPDATE_DATE) MAX_SRC_UPDATE_DATE FROM FHP.FHP_MEMBER_MASTER "
			+ " UNION  SELECT MAX(SRC_ROW_UPDATE_DATE) MAX_SRC_UPDATE_DATE FROM FHP.FHP_MEMBER_ELIGIBILITY UNION SELECT MAX(SRC_ROW_UPDATE_DATE)"
			+ " MAX_SRC_UPDATE_DATE FROM FHP.FHP_MEMBER_LABS)";

	private final String sqlMemberMergeQuery = "SELECT A.MEMBER_ROW_ID_PRIMARY, B.SALESFORCE_ID as MEMBER_PRIMARY_SALESFORCE_ID, B.MEDICAL_RECORD_NUMBER as MEMBER_PRIMARY_MRN, \n" + 
			"A.MEMBER_ROW_ID_MERGE, C.SALESFORCE_ID AS MEMBER_MERGE_SALESFORCE_ID, C.MEDICAL_RECORD_NUMBER as MEMBER_MERGE_MRN\n" + 
			"FROM FHP.FHP_Member_Merge_List A\n" + 
			"JOIN  FHP.FHP_MEMBER_MASTER B On B.SRC_MEMBER_ROW_ID = MEMBER_ROW_ID_PRIMARY\n" + 
			"JOIN FHP.FHP_MEMBER_MASTER C On C.SRC_MEMBER_ROW_ID = MEMBER_ROW_ID_MERGE\n" + 
			"WHERE A.ROW_UPDATE_DATE > ?\n" + 
			"AND B.SALESFORCE_ID IS NOT NULL\n" + 
			"AND C.SALESFORCE_ID IS NOT NULL" ;
	
    private final String sqlMemberSplitQuery = "SELECT A.MEMBER_ROW_ID_ORIGINAL, B.SALESFORCE_ID AS MEMBER_ORIGINAL_SALESFORCE_ID, B.MEDICAL_RECORD_NUMBER as MEMBER_ORIGINAL_MRN,\n" + 
    		"A.MEMBER_ROW_ID_NEW, C.SALESFORCE_ID AS MEMBER_NEW_SALESFORCE_ID, C.MEDICAL_RECORD_NUMBER as MEMBER_NEW_MRN\n" + 
	  		"FROM FHP.FHP_MEMBER_SPLIT_LIST A\n" + 
	  		"JOIN  FHP.FHP_MEMBER_MASTER B On B.SRC_MEMBER_ROW_ID = A.MEMBER_ROW_ID_ORIGINAL\n" + 
	  		"JOIN  FHP.FHP_MEMBER_MASTER C On C.SRC_MEMBER_ROW_ID = A.MEMBER_ROW_ID_NEW\n" + 
	  		"WHERE ROW_UPDATE_DATE > ?\n" + 
	  		"AND B.SALESFORCE_ID IS NOT NULL";
	 
    private final String sqlHandshakeSplitMergeCountQuery = "SELECT COUNT(*) FROM SFHCADM.PROCESS_HANDSHAKE WHERE PROCESS_NAME = ?";

	private final String sqlHandshakeSplitMergeDeleteQuery = "DELETE FROM SFHCADM.PROCESS_HANDSHAKE WHERE PROCESS_NAME = ?";

	private final String sqlMergeMemberStatusUpdateQuery ="UPDATE FHP.FHP_MEMBER_MERGE_LIST\n"
			+ "SET SFHC_LOAD_FLAG = ?, SFHC_LOAD_RESPONSE_DTTM = ? ,SFHC_LOAD_FAILURE_CODE = ? , SHFC_LOAD_FAILURE_REASON = ?\n"
			+ "WHERE MEMBER_ROW_ID_PRIMARY = ?";
	
	private final String sqlSplitMemberStatusUpdateQuery ="UPDATE FHP.FHP_MEMBER_SPLIT_LIST\n"
			+ "SET SFHC_LOAD_FLAG = ?, SFHC_LOAD_RESPONSE_DTTM = ? ,SFHC_LOAD_FAILURE_CODE = ? , SHFC_LOAD_FAILURE_REASON = ?\n"
			+ "WHERE MEMBER_ROW_ID_ORIGINAL = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public MemberStagingData getMemberStagingData( long id, Date lastMaxSrcUpdateTime ) throws ServiceException {
		MemberStagingData memberStagingData = new MemberStagingData();

		try {
			memberStagingData.setMemberMaster(this.getMemberMaster(id));
			memberStagingData.setMemberEligibilityList(this.getMemberEligibility(id));

			if (lastMaxSrcUpdateTime != null) {
				memberStagingData.setMemberLabsList(this.getMemberLabs(id, lastMaxSrcUpdateTime));
			} else {
				memberStagingData.setMemberLabsList(this.getMemberLabs(id));
			}
		}
		catch (ServiceException e) {
			logger.error("Error while getting data from Staging table for Member ID " + id );
			throw e;
		}
			
		return memberStagingData;
	}

	public MemberMaster getMemberMaster( long id ) throws ServiceException {
		MemberMaster member = null;

		try {
			member = jdbcTemplate.queryForObject(
					this.sqlMemberQuery, new Object[] { id }, new MemberRowMapper());
		} 
		catch( Exception e) {
			String errorMessage = "Error while getting data from Member Master Staging table for Member ID " + id;
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}

		return member;
	}


	public List<MemberEligibility> getMemberEligibility( long memberId ) throws ServiceException{
		List<MemberEligibility> memberList = null;

		try {
			memberList = jdbcTemplate.query(
					this.sqlMemberEligibilityQuery, 
					new Object[] { new Long(memberId) }, 
					new MemberEligibilityRowMapper());
		}
		catch( Exception e) {
			String errorMessage = "Error while getting data from Member Eligibility Staging table for Member ID " + memberId;
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}

		return memberList;
	}

	public List<MemberLabs> getMemberLabs( long memberId ) throws ServiceException {
		List<MemberLabs> memberLabsList = null;

		try {
			memberLabsList = jdbcTemplate.query(
					this.sqlMemberLabsQueryAll, 
					new Object[] { new Long(memberId) }, 
					new MemberLabsRowMapper());
		}
		catch( Exception e) {
			String errorMessage = "Error while getting data from Member Lab Staging table for Member ID " + memberId;
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}

		return memberLabsList;
	}

	public List<MemberLabs> getMemberLabs( long memberId, Date lastMaxSrcUpdateTime ) throws ServiceException {
		List<MemberLabs> memberLabsList = null;
		java.sql.Date refDate = new java.sql.Date( lastMaxSrcUpdateTime.getTime());
		try {
			memberLabsList = jdbcTemplate.query(
					this.sqlMemberLabsQuery, 
					new Object[] { new Long(memberId) , refDate }, 
					new MemberLabsRowMapper());
		}
		catch( Exception e) {
			String errorMessage = "Error while getting data from Member Lab Staging table for Member ID " + memberId;
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}

		return memberLabsList;
	}

	public List<Long> getUpdatedMemberIds( Date lastUpdateDate ) throws ServiceException {
		List<Long> updatedMemberIds = null;
		java.sql.Date refDate = new java.sql.Date( lastUpdateDate.getTime());
		try {
			updatedMemberIds = jdbcTemplate.queryForList(sqlUpdateMemberIds,
														new java.sql.Date[] {refDate, refDate, refDate},
														Long.class);
		}
		catch( Exception e) {
			e.printStackTrace();
			String errorMessage = "Error getting list of MemberIds from Staging tables";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage);
		}
		return updatedMemberIds ;
	}


	public Date getMaxSrcUpdateDate(){
		Date maxSrcUpdateDate;
		maxSrcUpdateDate = jdbcTemplate.queryForObject(sqlMaxSrcUpdateDate, java.sql.Date.class);
		return new Date(maxSrcUpdateDate.getTime()) ;
	}
	
	
	public void updateMemberResponse( MemberResponse response ) throws ServiceException {
		try {
			Object parameters[] = new Object[7];
			
			parameters[0] = response.getSalesForceID();
			parameters[1] = response.getMRN();
			parameters[2] = response.getEUID();
			parameters[3] = response.isSuccessfulHCUpsert() ? "Y" : "N";			
			parameters[4] = response.getFailureReason();
			parameters[5] = new Date();
			parameters[6] = response.getId();
			
			int paramTypes[] = { Types.NVARCHAR, Types.BIGINT, Types.BIGINT, Types.NVARCHAR, Types.NVARCHAR, Types.DATE, Types.BIGINT} ;
			logger.info("updateMemberResponse parameters ==> " + Arrays.toString(parameters));
			jdbcTemplate.update(sqlMemberStatusUpdateQuery, parameters, paramTypes);
		} 
		catch( Exception e) {
			e.printStackTrace();
			String errorMessage = "Error while updating data to Staging table for Member ID " + response.getId();
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}
		
	}

	public int getFHPDataReadiness( ) throws ServiceException {
		int count;

		try {
			count = jdbcTemplate.queryForObject (sqlHandshakeCountQuery, Integer.class).intValue();
		} catch( Exception e) {
			String errorMessage = "Error while checking the Process Handshake table in Staging database.";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}

		return count;
	}
	
	public void clearProcessHandshake( ) throws ServiceException {
		//String sqlQuery = "DELETE FROM SFHCADM.PROCESS_HANDSHAKE WHERE PROCESS_NAME='FHP_MEMBER_LOAD'";
		
		try {
			jdbcTemplate.execute(sqlHandshakeDeleteQuery); 
		} catch( Exception e) {
			String errorMessage = "Error while deleting from Process Handshake table in Staging database.";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}
		
	}

	/**
	 * 
	 * @param memberLoadAudit
	 * @throws ServiceException
	 */
	public void populateMemberLoadAudit(MemberLoadAudit memberLoadAudit) throws ServiceException{
		try {

			String empiSearchStringRequest = DateUtil.getTextUptoCharacters(memberLoadAudit.getEmpiSearchStringRequest(), 4000);
			String empiSearchStringResponse = DateUtil.getTextUptoCharacters(memberLoadAudit.getEmpiSearchStringResponse(), 4000);
			String comments = DateUtil.getTextUptoCharacters(memberLoadAudit.getComment(), 4000);
			String loadComments = DateUtil.getTextUptoCharacters(memberLoadAudit.getSfhcLoadComment(), 1000);

			Object[] params = new Object[] {
						memberLoadAudit.getSfhcLoadDttm(), memberLoadAudit.getSrcMemberId(), memberLoadAudit.getOperation(), memberLoadAudit.getFailureComponent(), memberLoadAudit.getSfhcLoadStatus(),
						loadComments, memberLoadAudit.getTgtMemberId(), memberLoadAudit.getSalesforceIdMatch1(), memberLoadAudit.getSalesforceIdMatch2(), 
						memberLoadAudit.getSalesforceIdMatch3(), memberLoadAudit.getSalesforceIdMatch4(), memberLoadAudit.getSalesforceIdMatch5(), memberLoadAudit.getSalesforceIdMatch6(), 
						memberLoadAudit.getSalesforceIdMatch7(), memberLoadAudit.getSalesforceIdMatch8(), memberLoadAudit.getSalesforceIdMatch9(), memberLoadAudit.getSalesforceIdMatch10(),
						memberLoadAudit.getMemberType(), memberLoadAudit.getFlowState(), comments, memberLoadAudit.getCategory(), memberLoadAudit.getMrnFromHc(), memberLoadAudit.getMrnFromEmpi(),
						empiSearchStringRequest, empiSearchStringResponse,
						memberLoadAudit.getMrnMatch1(), memberLoadAudit.getMrnMatch2(), memberLoadAudit.getMrnMatch3(), memberLoadAudit.getMrnMatch4(), memberLoadAudit.getMrnMatch5(),
						memberLoadAudit.getMrnMatch6(), memberLoadAudit.getMrnMatch7(), memberLoadAudit.getMrnMatch8(), memberLoadAudit.getMrnMatch9(), memberLoadAudit.getMrnMatch10(),
						memberLoadAudit.getSfIdFromHc(), memberLoadAudit.getSfIdFromEmpi()
					};
			int[] types = new int[] {
					Types.DATE, Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
					Types.VARCHAR, Types.NUMERIC, Types.NVARCHAR, Types.NVARCHAR, 
					Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR,  
					Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, 
					Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR,
					Types.NVARCHAR, Types.NVARCHAR, 
					Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR,
					Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR,
					Types.NVARCHAR, Types.NVARCHAR
			};
			
			jdbcTemplate.update(sqlMemberLoadAuditInsertQuery, params, types);
		}catch(Exception e) {
			String errorMessage = "populateMemberLoadAudit() - Error while Inserting in MemberLoadAudit table in Staging database.";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}
		
	}

	public List<MemberMergeStagingData> getMemberMergeStagingData(Date lastUpdateDate) throws ServiceException {
		List<MemberMergeStagingData> memberMergeStagingDataList = new ArrayList<MemberMergeStagingData>();

		try {
			memberMergeStagingDataList = jdbcTemplate.query(this.sqlMemberMergeQuery,
					new Object[] { new java.sql.Date(lastUpdateDate.getTime()) }, new MemberMergeRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error while getting Merge records from FHP_MEMBER_MERGE_LIST table in Staging database.";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);

		}

		return memberMergeStagingDataList;
	}

	public List<MemberSplitStagingData> getMemberSplitStagingData(Date lastUpdateDate) throws ServiceException {

		List<MemberSplitStagingData> memberSplitRequestDataList;
		try {
			memberSplitRequestDataList = jdbcTemplate.query(this.sqlMemberSplitQuery,
					new Object[] { new java.sql.Date(lastUpdateDate.getTime()) }, new MemberSplitRowMapper());
		
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error while getting Split records from FHP_MEMBER_SPLIT_LIST table in Staging database.";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);

		}

		return memberSplitRequestDataList;
	}

	public int getFHPSplitMergeDataReadiness(String processName) throws ServiceException {
		int count;

		try {
			count = jdbcTemplate.queryForObject(sqlHandshakeSplitMergeCountQuery,
					new Object[] { new String(processName) }, Integer.class).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error while checking the Process Handshake table in Staging database for " + processName + ".";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}

		return count;
	}

	public void clearSplitMergeProcessHandshake(String processName) throws ServiceException {

		try {
			jdbcTemplate.update(sqlHandshakeSplitMergeDeleteQuery, new Object[] { new String(processName) });
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error while deleting from Process Handshake table in Staging database.";
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}

	}
	
	public void updateSplitMergeMemberResponse( MemberSplitMergeResponse response ) throws ServiceException {
		try {
			String jobType = response.getType();
			String updateQuery = SplitMergeEnum.SPLIT.getType().equals(jobType) ? sqlSplitMemberStatusUpdateQuery : sqlMergeMemberStatusUpdateQuery;
			Object parameters[] = new Object[5];
			
			parameters[0] = response.isSuccess() ? "Y" : "N";
			parameters[1] = new Date();
			parameters[2] = response.getFailureCode();
			parameters[3] = response.getFailureReason();
			parameters[4] = response.getFhpId();

			int paramTypes[] = { Types.VARCHAR, Types.DATE, Types.NVARCHAR, Types.NVARCHAR, Types.BIGINT} ;
			logger.info("Update to {} List parameters ==> {} ", jobType,  Arrays.toString(parameters));

			jdbcTemplate.update(updateQuery, parameters, paramTypes);
		} 
		catch( Exception e) {
			e.printStackTrace();
			String errorMessage = "Error while updating data to Staging table for type : "+ response.getType() +" and FHP ID :" + response.getFhpId();
			logger.error("Error while updating data to Staging table for type : {} and FHP ID : {}",  response.getType(),  response.getFhpId());
			logger.error(errorMessage);
			throw new ServiceException(errorMessage, e);
		}
		
	}

}
