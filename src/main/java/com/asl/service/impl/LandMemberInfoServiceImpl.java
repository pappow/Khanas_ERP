package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cacus;
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
	public long save(Cacus cacus) {
		if(cacus == null) return 0;
		cacus.setZid(sessionManager.getBusinessId());
		cacus.setZauserid(getAuditUser());
		return landMemberInfoMapper.save(cacus);
	}

	@Transactional
	@Override
	public long update(Cacus cacus) {
		if(cacus == null) return 0;
		cacus.setZid(sessionManager.getBusinessId());
		cacus.setZuuserid(getAuditUser());
		return landMemberInfoMapper.update(cacus);
	}

	@Override
	public Cacus findByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return landMemberInfoMapper.findByXcus(xcus, sessionManager.getBusinessId());
	}



	

	@Override
	public List<Cacus> searchCacus(String xtype, String xcus){
		if(StringUtils.isBlank(xtype) || StringUtils.isBlank(xcus)) return Collections.emptyList();
		return landMemberInfoMapper.searchCacus(xtype, xcus.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public Cacus findByXphone(String xphone){
		if(StringUtils.isBlank(xphone)) return null;
		return landMemberInfoMapper.findByXphone(xphone, sessionManager.getBusinessId());
	}

	public List<Cacus> searchXorg(String xorg){
		if(StringUtils.isBlank(xorg)) return Collections.emptyList();
		return landMemberInfoMapper.searchXorg(xorg.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Cacus> searchXgcus(String xgcus){
		if(StringUtils.isBlank(xgcus)) return Collections.emptyList();
		return landMemberInfoMapper.searchXorg(xgcus.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public Cacus findCacusByXcuszid(String xcuszid) {
		if(StringUtils.isBlank(xcuszid)) return null;
		return landMemberInfoMapper.findCacusByXcuszid(xcuszid, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteCacus(String xcus) {
		if(StringUtils.isBlank(xcus)) return 0;
		return landMemberInfoMapper.deleteCacus(xcus, sessionManager.getBusinessId());
	}

	@Override
	public List<Cacus> getAllCacusByPrefix(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return landMemberInfoMapper.getAllCacusByPrefix(xtypetrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Cacus> getAllCacus() {
		return landMemberInfoMapper.getAllCacus(sessionManager.getBusinessId());
	}

	@Override
	public List<Cacus> getAllLandMembers() {
		return landMemberInfoMapper.getAllLandMembers(sessionManager.getBusinessId());
	}

	@Override
	public long delete(Cacus cacus) {
		if(cacus == null) return 0;
		long count = landMemberInfoMapper.delete(cacus);
		return count;
	}





}
