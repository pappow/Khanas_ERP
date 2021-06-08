package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandInfo;
import com.asl.mapper.LandInfoMapper;
import com.asl.service.LandInfoService;

@Service
public class LandInfoServiceImpl extends AbstractGenericService implements LandInfoService {

	@Autowired
	private LandInfoMapper landinfoMapper;
	
	@Transactional
	@Override
	public long save(LandInfo landInfo) {
		if (landInfo == null)
			return 0;
		landInfo.setZid(sessionManager.getBusinessId());
		landInfo.setZauserid(getAuditUser());
		return landinfoMapper.saveLandInfo(landInfo);
	}

	@Transactional
	@Override
	public long update(LandInfo landInfo) {
		if (landInfo == null)
			return 0;
		landInfo.setZid(sessionManager.getBusinessId());
		landInfo.setZuuserid(getAuditUser());
		return landinfoMapper.updateLandInfo(landInfo);
	}

	@Override
	public List<LandInfo> getAllLandInfo() {
		return landinfoMapper.getAllLandInfo(sessionManager.getBusinessId());
	}

	@Override
	public LandInfo findByLandInfo(String xland) {
		if (StringUtils.isBlank(xland))
			return null;
		return landinfoMapper.findByLandInfo(xland, sessionManager.getBusinessId());
	}

}
