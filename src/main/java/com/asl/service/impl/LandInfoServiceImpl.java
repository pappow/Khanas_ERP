package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandDagDetails;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
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
//for owner
	
	@Transactional
	@Override
	public long save(LandOwner landOwner) {
		if (landOwner == null)
			return 0;
		landOwner.setZid(sessionManager.getBusinessId());
		landOwner.setZauserid(getAuditUser());
		return landinfoMapper.saveLandOwner(landOwner);
	}
	@Transactional
	@Override
	public long update(LandOwner landOwner) {
		if (landOwner == null)
			return 0;
		landOwner.setZid(sessionManager.getBusinessId());
		landOwner.setZauserid(getAuditUser());
		return landinfoMapper.updateLandOwner(landOwner);
	}


	@Override
	public List<LandOwner> getAllLandOwner(String zid) {
		return landinfoMapper.getAllLandOwner(sessionManager.getBusinessId());
	}

	@Override
	public List<LandOwner> findByLandOwner(String xland) {
		if (StringUtils.isBlank(xland))
			return null;
		return landinfoMapper.findByLandOwner(xland, sessionManager.getBusinessId());
	}

	

	

	@Override
	public long deleteLandOwner(LandOwner landOwner) {
		if(landOwner == null) return 0;
		long count = landinfoMapper.deleteLandOwner(landOwner);
		return count;
	}

	@Override
	public LandOwner findByXlandAndXperson(String xland, String xperson) {
		if (StringUtils.isBlank(xland) || StringUtils.isBlank(xperson))
			return null;
		return landinfoMapper.findByXlandAndXperson(xland, xperson, sessionManager.getBusinessId());
	}

	@Override
	public LandOwner findLandOwnerByXlandAndXrow(String xland, int xrow) {
		if(StringUtils.isBlank(xland) || xrow == 0) return null;
		return landinfoMapper.findLandOwnerByXlandAndXrow(xland,xrow,sessionManager.getBusinessId());
	}

	//end of owner
	
	//start of dag details
	@Transactional
	@Override
	public long save(LandDagDetails landDagDetails) {
		if (landDagDetails == null)
			return 0;
		landDagDetails.setZid(sessionManager.getBusinessId());
		landDagDetails.setZauserid(getAuditUser());
		return landinfoMapper.saveLandDagDetails(landDagDetails);
	}

	@Transactional
	@Override
	public long update(LandDagDetails landDagDetails) {
		if (landDagDetails == null)
			return 0;
		landDagDetails.setZid(sessionManager.getBusinessId());
		landDagDetails.setZauserid(getAuditUser());
		return landinfoMapper.updateLandDagDetails(landDagDetails);
	}

	@Override
	public long deleteLandDagDetails(LandDagDetails landDagDetails) {
		if(landDagDetails == null) return 0;
		long count = landinfoMapper.deleteLandDagDetails(landDagDetails);
		return count;
	}

	@Override
	public List<LandDagDetails> getAllLandDagDetails(String zid) {
		return landinfoMapper.getAllLandDagDetails(sessionManager.getBusinessId());
	}

	@Override
	public List<LandDagDetails> findByLandDagDetails(String xland) {
		if (StringUtils.isBlank(xland))
			return null;
		return landinfoMapper.findByLandDagDetails(xland, sessionManager.getBusinessId());
	}

	@Override
	public LandDagDetails findlandDagDetailsByXlandAndXrow(String xland, int xrow) {
		if(StringUtils.isBlank(xland) || xrow == 0) return null;
		return landinfoMapper.findlandDagDetailsByXlandAndXrow(xland,xrow,sessionManager.getBusinessId());
	}

	

	


}
