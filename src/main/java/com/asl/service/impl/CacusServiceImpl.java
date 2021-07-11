package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cacus;
import com.asl.mapper.CacusMapper;
import com.asl.service.CacusService;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Service
public class CacusServiceImpl extends AbstractGenericService implements CacusService {

	@Autowired private CacusMapper cacusMapper;

	@Transactional
	@Override
	public long save(Cacus cacus) {
		if(cacus == null || StringUtils.isBlank(cacus.getXtype()) || StringUtils.isBlank(cacus.getXcustype())) return 0;
		cacus.setZid(sessionManager.getBusinessId());
		cacus.setZauserid(getAuditUser());
		return cacusMapper.save(cacus);
	}

	@Transactional
	@Override
	public long update(Cacus cacus) {
		if(cacus == null || StringUtils.isBlank(cacus.getXtype()) || StringUtils.isBlank(cacus.getXcustype())) return 0;
		cacus.setZid(sessionManager.getBusinessId());
		cacus.setZuuserid(getAuditUser());
		return cacusMapper.update(cacus);
	}

	@Override
	public Cacus findByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return cacusMapper.findByXcus(xcus, sessionManager.getBusinessId());
	}

	@Override
	public List<Cacus> findByXtype(String xtype) {
		if(StringUtils.isBlank(xtype)) return null;
		return cacusMapper.findByXtype(xtype, sessionManager.getBusinessId());
	}

	@Override
	public List<Cacus> getAllCacus() {
		return cacusMapper.getAllCacus(sessionManager.getBusinessId());
	}

	@Override
	public List<Cacus> searchCacus(String xtype, String xcus){
		if(StringUtils.isBlank(xtype) || StringUtils.isBlank(xcus)) return Collections.emptyList();
		return cacusMapper.searchCacus(xtype, xcus.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Cacus> searchCustomer(String xtype, String xcus) {
		if(StringUtils.isBlank(xtype) || StringUtils.isBlank(xcus)) return Collections.emptyList();
		return cacusMapper.searchCustomer(xtype, xcus.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public Cacus findByXphone(String xphone){
		if(StringUtils.isBlank(xphone)) return null;
		return cacusMapper.findByXphone(xphone, sessionManager.getBusinessId());
	}

	public List<Cacus> searchXorg(String xorg){
		if(StringUtils.isBlank(xorg)) return Collections.emptyList();
		return cacusMapper.searchXorg(xorg.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Cacus> searchXgcus(String xgcus){
		if(StringUtils.isBlank(xgcus)) return Collections.emptyList();
		return cacusMapper.searchXorg(xgcus.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public Cacus findCacusByXcuszid(String xcuszid) {
		if(StringUtils.isBlank(xcuszid)) return null;
		return cacusMapper.findCacusByXcuszid(xcuszid, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteCacus(String xcus) {
		if(StringUtils.isBlank(xcus)) return 0;
		return cacusMapper.deleteCacus(xcus, sessionManager.getBusinessId());
	}

	

	

	
}
