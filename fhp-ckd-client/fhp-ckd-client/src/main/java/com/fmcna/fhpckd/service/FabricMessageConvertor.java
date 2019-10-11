package com.fmcna.fhpckd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmcna.fhpckd.beans.staging.MemberEligibility;
import com.fmcna.fhpckd.beans.staging.MemberLabs;
import com.fmcna.fhpckd.beans.staging.MemberLoadAudit;
import com.fmcna.fhpckd.beans.staging.MemberMaster;
import com.fmcna.fhpckd.beans.staging.MemberMerge;
import com.fmcna.fhpckd.beans.staging.MemberResponse;
import com.fmcna.fhpckd.beans.staging.MemberSplit;
import com.fmcna.fhpckd.beans.staging.MemberSplitMergeResponse;
import com.fmcna.fhpckd.beans.staging.MemberStagingData;
import com.fmcna.fhpckd.beans.staging.SplitMergeMember;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.model.FhpCkdRequest;
import com.fmcna.fhpckd.model.FhpCkdResponse;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;
import com.fmcna.fhpckd.model.FhpSplitMergeResponse;
import com.fmcna.fhpckd.model.PatientDemographics;
import com.fmcna.fhpckd.model.PatientLabData;
import com.fmcna.fhpckd.model.PatientProgramEligibility;

/**
 * 
 * This class converts Staging table DTOs to objects for FARBIC request.
 * 
 * @author manishtiwari
 *
 */
public class FabricMessageConvertor {

	private static final Logger logger = LoggerFactory.getLogger(FabricMessageConvertor.class);

	/**
	 * This method converts the staging table DTO to Fabric Request.
	 * 
	 * @param stagingData The DTO that has data from staging tables.
	 * @return Object for Fabric Request
	 */

	public FhpCkdRequest generateFabricRequest(MemberStagingData stagingData) {

		logger.debug("Started generateFabricRequest()");
		FhpCkdRequest request = new FhpCkdRequest();

		MemberMaster memberMaster = stagingData.getMemberMaster();

		List<MemberEligibility> memberEligibilityList = stagingData.getMemberEligibilityList();

		List<MemberLabs> memberLabsList = stagingData.getMemberLabsList();

		if (memberMaster == null)
			return null;

		request.setFhpId(memberMaster.getId().toString());
		request.setDemographics(mapPatientDemographics(memberMaster));
		request.setLabs(this.mapPatientLabs(memberLabsList));
		request.setProgramEligibilities(this.mapProgramEligibility(memberEligibilityList));

		return request;
	}

	/**
	 * This method converts the staging table DTO to Patient Demographics object of
	 * Fabric Request.
	 * 
	 * 
	 * @param memberMaster The staging table DTO for Member Master.
	 * @return Patient Demographics object for Fabric Request
	 */
	private PatientDemographics mapPatientDemographics(MemberMaster memberMaster) {
		logger.debug("Started mapPatientDemographics()");

		PatientDemographics patientDemographics = new PatientDemographics();

		patientDemographics.setSsn(memberMaster.getSSN());

		patientDemographics.setAddress1(memberMaster.getAddress1());
		patientDemographics.setAddress2(memberMaster.getAddress2());
		patientDemographics.setAddress3(memberMaster.getAddress3());
		patientDemographics.setCity(memberMaster.getCity());
		patientDemographics.setState(memberMaster.getState());
		patientDemographics.setZip(memberMaster.getZip());
		patientDemographics.setCounty(memberMaster.getCounty());
		patientDemographics.setIsland(memberMaster.getIsland());
		patientDemographics.setCountry(memberMaster.getCountry());
		patientDemographics.setPhone(memberMaster.getPhone());
		patientDemographics.setAlternatePhone(memberMaster.getAlternatePhone());
		patientDemographics.setEmergencyPhone(memberMaster.getEmergencyPhone());
		patientDemographics.setEveningPhone(memberMaster.getEveningPhone());
		patientDemographics.setFax(memberMaster.getFax());
		patientDemographics.setFirstName(memberMaster.getFirstName());
		patientDemographics.setMiddleName(memberMaster.getMiddleName());
		patientDemographics.setLastName(memberMaster.getLastName());
		patientDemographics.setDob(memberMaster.getDateOfBirth());
		patientDemographics.setGender(memberMaster.getGender());
		patientDemographics.setRace(memberMaster.getRace());
		patientDemographics.setEmail(memberMaster.getEmail());
		patientDemographics.setStatus(memberMaster.getStatus());
		patientDemographics.setLobVendor(memberMaster.getLobVendor());
		patientDemographics.setLobType(memberMaster.getLobType());
		patientDemographics.setHicn(memberMaster.getHicn());
		patientDemographics.setClaimSubscriberId(memberMaster.getClaimSubscriberID());
		patientDemographics.setMbi(memberMaster.getMbi());
		patientDemographics.setMedicaidNo(memberMaster.getMedicaidNo());

		patientDemographics.setSourceId(memberMaster.getId());

		patientDemographics.setLatestEligibilityMemberId(memberMaster.getLatestEligibilityMemberId());

		if (memberMaster.getMRN() != null && memberMaster.getMRN().longValue() != 0) {
			patientDemographics.setMrn(memberMaster.getMRN().toString());
		}

		return patientDemographics;
	}

	/**
	 * This method converts the staging table DTO to Patient Lab object of Fabric
	 * Request.
	 * 
	 * @param memberLabsList
	 * @return Patient Lab object for Fabric Request
	 */
	private List<PatientLabData> mapPatientLabs(List<MemberLabs> memberLabsList) {
		logger.debug("Started mapPatientLabs()");

		List<PatientLabData> patientLabList = new ArrayList<PatientLabData>();

		if (memberLabsList == null)
			return null;

		for (MemberLabs m : memberLabsList) {
			PatientLabData labData = new PatientLabData();

			labData.setServiceDate(m.getServiceDate());

			labData.setLabType(m.getLabType());
			if (m.getLabResult() != null) {
				labData.setLabResult(m.getLabResult().toPlainString());
			}
			labData.setLabLoincCode(m.getLabLoincCode());

			if (m.getId() != null) {
				labData.setSourceId(m.getId());
			}
			if (m.getRowDeleted() != null) {
				if (m.getRowDeleted().equals("Y")) {
					labData.setDeleted(true);
				} else if (m.getRowDeleted().equals("N")) {
					labData.setDeleted(false);
				}
			}

			patientLabList.add(labData);
		}

		return patientLabList;
	}

	/**
	 * This method converts the staging table DTO to Patient Eligibility object of
	 * Fabric Request.
	 * 
	 * @param memberEligibilityList Member Eligibility List DTO from Staging table.
	 * @return Patient Eligibility List object for Fabric Request
	 */
	private List<PatientProgramEligibility> mapProgramEligibility(List<MemberEligibility> memberEligibilityList) {
		logger.debug("Started mapProgramEligibility()");

		List<PatientProgramEligibility> programEligibilityList = new ArrayList<PatientProgramEligibility>();

		if (memberEligibilityList == null)
			return null;

		for (MemberEligibility me : memberEligibilityList) {
			PatientProgramEligibility programEligibility = new PatientProgramEligibility();

			programEligibility.setStartDate(me.getStartDate());
			programEligibility.setTermDate(me.getTermDate());
			programEligibility.setTermReason(me.getTermReason());
			programEligibility.setStatus(me.getStatus());
			programEligibility.setLobVendor(me.getLobVendor());
			programEligibility.setLobType(me.getLobType());
			programEligibility.setSourceId(me.getId());
			if (me.getRowDeleted() != null) {
				if (me.getRowDeleted().equals("Y")) {
					programEligibility.setDeleted(true);
				} else if (me.getRowDeleted().equals("N")) {
					programEligibility.setDeleted(false);
				}
			}

			programEligibility.setHclob(me.getHclob());
			programEligibility.setHcGroup(me.getHcGroup());
			programEligibility.setHcProgram(me.getHcProgram());

			programEligibilityList.add(programEligibility);
		}

		return programEligibilityList;
	}

	public MemberResponse parseFabricResponse(FhpCkdResponse fabricResponse) {
		String value;

		MemberResponse memberResponse = new MemberResponse();
		memberResponse.setId(Long.parseLong(fabricResponse.getFhpId()));
		memberResponse.setSalesForceID(((fabricResponse.getSfIdFromHc() == null) ? fabricResponse.getSfIdFromEmpi() : fabricResponse.getSfIdFromHc()));

		value = fabricResponse.getEuId();
		if (value != null) {
			memberResponse.setEUID(Long.parseLong(value));
		}

		value = null;
		if (fabricResponse.getMrn() != null) {
			value = fabricResponse.getMrn();
		} else if (fabricResponse.getMrnFromHc() != null) {
			value = fabricResponse.getMrnFromHc();
		} else if (fabricResponse.getMrnFromEmpi() != null) {
			value = fabricResponse.getMrnFromEmpi();
		}

		if (value != null) {
			memberResponse.setMRN(Long.parseLong(value));
		}

		memberResponse.setSuccessfulHCUpsert(fabricResponse.getSuccess().booleanValue());

		StringBuffer strFailureMessage = new StringBuffer();

		if (fabricResponse.getFailureCode() != null) {
			strFailureMessage.append(fabricResponse.getFailureCode()).append(" - ");
		}
		if (fabricResponse.getFailureReason() != null) {
			strFailureMessage.append(fabricResponse.getFailureReason());
		}
		memberResponse.setFailureReason(strFailureMessage.toString());

		return memberResponse;
	}

	public MemberLoadAudit mapMemberLoadAudit(FhpCkdResponse fabricResponse) {
		MemberLoadAudit memberLoadAudit = new MemberLoadAudit();
		memberLoadAudit.setSfhcLoadDttm(new Date());// TODO- look at date format
		if (fabricResponse.getFhpId() != null) {
			memberLoadAudit.setSrcMemberId(Long.valueOf(fabricResponse.getFhpId()));
		}

		memberLoadAudit.setSfIdFromHc(fabricResponse.getSfIdFromHc());
		memberLoadAudit.setSfIdFromEmpi(fabricResponse.getSfIdFromEmpi());

		if (fabricResponse.getFailureReason() != null) {
			memberLoadAudit.setSfhcLoadComment(fabricResponse.getFailureReason());
		}

		if (fabricResponse.getSuccess()) {
			memberLoadAudit.setSalesforceIdMatch1((((fabricResponse.getSfIdFromHc() == null) ? fabricResponse.getSfIdFromEmpi() : fabricResponse.getSfIdFromHc())));
			memberLoadAudit.setSfhcLoadStatus("Y");
		} else {
			memberLoadAudit.setSfhcLoadStatus("N");
			if (fabricResponse.getDuplicateSalesForceIDs() != null
					&& !fabricResponse.getDuplicateSalesForceIDs().isEmpty()) {
				List<String> sfIdList = fabricResponse.getDuplicateSalesForceIDs();
				if (sfIdList.size() >= 1 && sfIdList.get(0) != null) {
					memberLoadAudit.setSalesforceIdMatch1(sfIdList.get(0));
				}
				if (sfIdList.size() >= 2 && sfIdList.get(1) != null) {
					memberLoadAudit.setSalesforceIdMatch2(sfIdList.get(1));
				}
				if (sfIdList.size() >= 3 && sfIdList.get(2) != null) {
					memberLoadAudit.setSalesforceIdMatch3(sfIdList.get(2));
				}

				if (sfIdList.size() >= 4 && sfIdList.get(3) != null) {
					memberLoadAudit.setSalesforceIdMatch4(sfIdList.get(3));
				}
				if (sfIdList.size() >= 5 && sfIdList.get(4) != null) {
					memberLoadAudit.setSalesforceIdMatch5(sfIdList.get(4));
				}

				if (sfIdList.size() >= 6 && sfIdList.get(5) != null) {
					memberLoadAudit.setSalesforceIdMatch6(sfIdList.get(5));
				}
				if (sfIdList.size() >= 7 && sfIdList.get(6) != null) {
					memberLoadAudit.setSalesforceIdMatch7(sfIdList.get(6));
				}

				if (sfIdList.size() >= 8 && sfIdList.get(7) != null) {
					memberLoadAudit.setSalesforceIdMatch8(sfIdList.get(7));
				}
				if (sfIdList.size() >= 9 && sfIdList.get(8) != null) {
					memberLoadAudit.setSalesforceIdMatch9(sfIdList.get(8));
				}
				if (sfIdList.size() >= 10 && sfIdList.get(9) != null) {
					memberLoadAudit.setSalesforceIdMatch10(sfIdList.get(9));
				}
			}

			if (fabricResponse.getDuplicateMrns() != null
					&& !fabricResponse.getDuplicateMrns().isEmpty()) {
				List<String> mrnList = fabricResponse.getDuplicateMrns();
				if (mrnList.size() >= 1 && mrnList.get(0) != null) {
					memberLoadAudit.setMrnMatch1(mrnList.get(0));
				}
				if (mrnList.size() >= 2 && mrnList.get(1) != null) {
					memberLoadAudit.setMrnMatch2(mrnList.get(1));
				}
				if (mrnList.size() >= 3 && mrnList.get(2) != null) {
					memberLoadAudit.setMrnMatch3(mrnList.get(2));
				}

				if (mrnList.size() >= 4 && mrnList.get(3) != null) {
					memberLoadAudit.setMrnMatch4(mrnList.get(3));
				}
				if (mrnList.size() >= 5 && mrnList.get(4) != null) {
					memberLoadAudit.setMrnMatch5(mrnList.get(4));
				}

				if (mrnList.size() >= 6 && mrnList.get(5) != null) {
					memberLoadAudit.setMrnMatch6(mrnList.get(5));
				}
				if (mrnList.size() >= 7 && mrnList.get(6) != null) {
					memberLoadAudit.setMrnMatch7(mrnList.get(6));
				}

				if (mrnList.size() >= 8 && mrnList.get(7) != null) {
					memberLoadAudit.setMrnMatch8(mrnList.get(7));
				}
				if (mrnList.size() >= 9 && mrnList.get(8) != null) {
					memberLoadAudit.setMrnMatch9(mrnList.get(8));
				}
				if (mrnList.size() >= 10 && mrnList.get(9) != null) {
					memberLoadAudit.setMrnMatch10(mrnList.get(9));
				}
			}

		}

		memberLoadAudit.setOperation(fabricResponse.getHcOperation());			
		memberLoadAudit.setMemberType(fabricResponse.getMemberType());
		memberLoadAudit.setFlowState(fabricResponse.getFlowState());
		memberLoadAudit.setComment(fabricResponse.getComment());
		memberLoadAudit.setCategory(fabricResponse.getCategory());			
		memberLoadAudit.setMrnFromHc(fabricResponse.getMrnFromHc());
		memberLoadAudit.setMrnFromEmpi(fabricResponse.getMrnFromEmpi());
		memberLoadAudit.setEmpiSearchStringRequest(fabricResponse.getEmpiSearchByDemoRequest());
		memberLoadAudit.setEmpiSearchStringResponse(fabricResponse.getEmpiSearchByDemoResponse());

		if (fabricResponse.getFhpIdFromHc() != null) {
			memberLoadAudit.setTgtMemberId(Long.valueOf(fabricResponse.getFhpIdFromHc()));
		}

		return memberLoadAudit;

	}

	public FhpSplitMergeRequest generateFabricMergeRequest(MemberMerge memberMerge) {

		logger.debug("Started generateFabricMergeRequest()");
		FhpSplitMergeRequest request = new FhpSplitMergeRequest();
		SplitMergeMember primaryMember = memberMerge.getPrimary();
		SplitMergeMember mergeMember = memberMerge.getMerge();

		request.setType(SplitMergeEnum.MERGE.getType());
		
		request.setRowIdPrimary(primaryMember.getMemberRowId().toString());
		request.setSfIdPrimary(primaryMember.getMemberSalesforceId());
		request.setMrnPrimary(primaryMember.getMemberMrn());
		
		request.setRowIdMerge(mergeMember.getMemberRowId().toString());
		request.setSfIdMerge(mergeMember.getMemberSalesforceId());
		request.setMrnMerge(mergeMember.getMemberMrn());

		return request;
	}

	public FhpSplitMergeRequest generateFabricSplitRequest(MemberSplit memberSplit) {

		logger.debug("Started generateFabricSplitRequest()");
		FhpSplitMergeRequest request = new FhpSplitMergeRequest();
		SplitMergeMember originalMember = memberSplit.getOriginal();

		request.setType(SplitMergeEnum.SPLIT.getType());

		request.setRowIdOriginal(originalMember.getMemberRowId().toString());
		request.setSfIdOriginal(originalMember.getMemberSalesforceId());
		request.setMrnOriginal(originalMember.getMemberMrn());
		
		List<SplitMergeMember> splitMergeList = memberSplit.getNewRows();
		int splitMergeListSize = splitMergeList.size();
		
		if (splitMergeListSize >= 1 && splitMergeList.get(0) != null) {
			request.setRowIdNew1(splitMergeList.get(0).getMemberRowId().toString());
		}

		if (splitMergeListSize >= 2 && splitMergeList.get(1) != null) {
			request.setRowIdNew2(splitMergeList.get(1).getMemberRowId().toString());
		}

		if (splitMergeListSize >= 3 && splitMergeList.get(2) != null) {
			request.setRowIdNew3(splitMergeList.get(2).getMemberRowId().toString());
		}

		if (splitMergeListSize >= 4 && splitMergeList.get(3) != null) {
			request.setRowIdNew4(splitMergeList.get(3).getMemberRowId().toString());
		}

		if (splitMergeListSize >= 5 && splitMergeList.get(4) != null) {
			request.setRowIdNew5(splitMergeList.get(4).getMemberRowId().toString());
		}

		return request;
	}

	public MemberSplitMergeResponse parseFabricResponse(FhpSplitMergeResponse fabricResponse) {
		MemberSplitMergeResponse memberResponse = new MemberSplitMergeResponse();

		memberResponse.setType(fabricResponse.getType());
		memberResponse.setFhpId(Long.parseLong(fabricResponse.getFhpId()));
		memberResponse.setSuccess(fabricResponse.getSuccess().booleanValue());

		memberResponse.setFailureCode(fabricResponse.getFailureCode());
		memberResponse.setFailureReason(fabricResponse.getFailureReason());

		return memberResponse;
	}

}
