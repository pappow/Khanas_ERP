package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Override
	public long save(Cacus cacus) {
		if(cacus == null || StringUtils.isBlank(cacus.getXtype()) || StringUtils.isBlank(cacus.getXcustype())) return 0;
		cacus.setZid(sessionManager.getBusinessId());
		return cacusMapper.save(cacus);
	}

	@Override
	public long update(Cacus cacus) {
		if(cacus == null || StringUtils.isBlank(cacus.getXtype()) || StringUtils.isBlank(cacus.getXcustype())) return 0;
		cacus.setZid(sessionManager.getBusinessId());
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
		return cacusMapper.searchCacus(xtype, xcus, sessionManager.getBusinessId());
	}

}
