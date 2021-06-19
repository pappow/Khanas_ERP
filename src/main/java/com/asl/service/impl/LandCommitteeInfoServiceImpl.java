package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.LandCommitteeInfo;
import com.asl.mapper.LandCommitteeInfoMapper;
import com.asl.service.LandCommitteeInfoService;
@Service
public class LandCommitteeInfoServiceImpl extends AbstractGenericService implements LandCommitteeInfoService{

	@Autowired private LandCommitteeInfoMapper  landcommitteeinfoMapper;
	@Override
	public long save(LandCommitteeInfo landCommiteeinfo) {
		if (landCommiteeinfo == null)
			return 0;
		landCommiteeinfo.setZid(sessionManager.getBusinessId());
		landCommiteeinfo.setZauserid(getAuditUser());
		return landcommitteeinfoMapper.saveLandCommitteeInfo(landCommiteeinfo);
	}

	@Override
	public long update(LandCommitteeInfo landCommiteeinfo) {
		if (landCommiteeinfo == null)
			return 0;
		landCommiteeinfo.setZid(sessionManager.getBusinessId());
		landCommiteeinfo.setZauserid(getAuditUser());
		return landcommitteeinfoMapper.updateLandCommitteeInfo(landCommiteeinfo);
	}

	@Override
	public List<LandCommitteeInfo> getAllLandCommitteeInfo() {
		return landcommitteeinfoMapper.getAllLandCommitteeInfo(sessionManager.getBusinessId());
	}

	@Override
	public LandCommitteeInfo findByLandCommitteeInfo(String xcommittee) {
		if (StringUtils.isBlank(xcommittee))
			return null;
		return landcommitteeinfoMapper.findByLandCommitteeInfo(xcommittee, sessionManager.getBusinessId());
	}

}
