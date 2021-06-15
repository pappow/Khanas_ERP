package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Caitem;
import com.asl.entity.LandMemberInfo;
import com.asl.entity.LandPerson;
import com.asl.mapper.LandMemberInfoMapper;
import com.asl.service.LandMemberInfoService;


@Service
public class LandMemberInfoServiceImpl extends AbstractGenericService implements LandMemberInfoService{

	@Autowired
	private LandMemberInfoMapper landMemberInfoMapper;
	
	@Transactional
	@Override
	public long save(LandMemberInfo landMemberInfo) {
		if (landMemberInfo == null)
			return 0;
		landMemberInfo.setZid(sessionManager.getBusinessId());
		landMemberInfo.setZauserid(getAuditUser());
		return landMemberInfoMapper.saveLandMemberInfo(landMemberInfo);
	}

	@Transactional
	@Override
	public long update(LandMemberInfo landMemberInfo) {
		if (landMemberInfo == null)
			return 0;
		landMemberInfo.setZid(sessionManager.getBusinessId());
		landMemberInfo.setZauserid(getAuditUser());
		return landMemberInfoMapper.updateLandMemberInfo(landMemberInfo);
	}

	@Override
	public List<LandMemberInfo> getAllLandMemberInfo() {
		return landMemberInfoMapper.getAllLandMemberInfo(sessionManager.getBusinessId());
	}

	@Override
	public LandMemberInfo findByLandMemberInfo(String xmember) {
		if (StringUtils.isBlank(xmember))
			return null;
		return landMemberInfoMapper.findByLandMemberInfo(xmember, sessionManager.getBusinessId());
	}

	


}
