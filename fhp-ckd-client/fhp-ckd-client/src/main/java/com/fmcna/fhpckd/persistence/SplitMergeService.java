package com.fmcna.fhpckd.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmcna.fhpckd.beans.staging.MemberMerge;
import com.fmcna.fhpckd.beans.staging.MemberMergeStagingData;
import com.fmcna.fhpckd.beans.staging.MemberSplit;
import com.fmcna.fhpckd.beans.staging.MemberSplitMergeResponse;
import com.fmcna.fhpckd.beans.staging.MemberSplitStagingData;
import com.fmcna.fhpckd.beans.staging.SplitMergeMember;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.staging.StagingDAO;

@Service
public class SplitMergeService {
	private static final Logger logger = LoggerFactory.getLogger(SplitMergeService.class);
	
	@Autowired
	private StagingDAO stagingDAO;

	public List<MemberMerge> getMemberMergeRequests(Date lastTimeStamp) throws ServiceException {
		List<MemberMerge> memberMergeList = new ArrayList<MemberMerge>();

		List<MemberMergeStagingData> memberMergeStagingDataList = this.stagingDAO
				.getMemberMergeStagingData(lastTimeStamp);

		for (MemberMergeStagingData memberMergeStagingData : memberMergeStagingDataList) {

			MemberMerge memberMerge = new MemberMerge();
			memberMerge.setPrimary(
					new SplitMergeMember(memberMergeStagingData.getRowIdPrimary(), memberMergeStagingData.getSfIdPrimary(), memberMergeStagingData.getMrnPrimary()));
			memberMerge.setMerge(
					new SplitMergeMember(memberMergeStagingData.getRowIdMerge(), memberMergeStagingData.getSfIdMerge(), memberMergeStagingData.getMrnMerge()));
			memberMergeList.add(memberMerge);
		}

		return memberMergeList;

	}

	public List<MemberSplit> getMemberSplitRequests(Date lastTimeStamp) throws ServiceException {
		List<MemberSplit> memberSplitList = new ArrayList<MemberSplit>();

		List<MemberSplitStagingData> memberMergeStagingDataList = this.stagingDAO
				.getMemberSplitStagingData(lastTimeStamp);

		for (MemberSplitStagingData memberSplitStagingData : memberMergeStagingDataList) {

			MemberSplit memberSplit = memberSplitList.stream().filter(
					member -> (memberSplitStagingData.getRowIdOriginal().equals(member.getOriginal().getMemberRowId())))
					.findAny().orElse(null);

			if (memberSplit == null) {
				memberSplit = new MemberSplit();
				memberSplit.setOriginal(new SplitMergeMember(memberSplitStagingData.getRowIdOriginal(),
						memberSplitStagingData.getSfIdOriginal(), memberSplitStagingData.getMrnOriginal()));
				memberSplit.addNewRow(new SplitMergeMember(memberSplitStagingData.getRowIdNew(), memberSplitStagingData.getSfIdNew(), memberSplitStagingData.getMrnNew()));
				
				memberSplitList.add(memberSplit);
			}  else {
				memberSplit.addNewRow(new SplitMergeMember(memberSplitStagingData.getRowIdNew(), memberSplitStagingData.getSfIdNew(), memberSplitStagingData.getMrnNew()));
				
			}

		}
		return memberSplitList;
	}

	public Date getMaxSrcUpdateDate() {
		return this.stagingDAO.getMaxSrcUpdateDate();
	}
	
	public boolean hasNewStagingData(String type) throws ServiceException {

		if (this.stagingDAO.getFHPSplitMergeDataReadiness(getHandShakeProcessname(type)) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void clearHandshakeFlag(String type) {
		try {
			this.stagingDAO.clearSplitMergeProcessHandshake(getHandShakeProcessname(type));
		} catch (ServiceException e) {
			logger.error("Error while clearing the handshake flag for {}", type);
		}
	}

	private String getHandShakeProcessname(String type) {
		return SplitMergeEnum.SPLIT.getType().equals(type) ? "FHP_MEMBER_SPLIT_LIST" : "FHP_MEMBER_MERGE_LIST";
	}

	public void updateSplitMergeResponse(String type, MemberSplitMergeResponse memberSplitMergeResponse)
			throws ServiceException {

		stagingDAO.updateSplitMergeMemberResponse(memberSplitMergeResponse);
	}

}