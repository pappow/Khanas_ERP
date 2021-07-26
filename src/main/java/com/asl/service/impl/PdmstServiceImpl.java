package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Pdmst;
import com.asl.mapper.PdmstMapper;
import com.asl.service.PdmstService;

/**
 * @author Zubayer Ahamed
 * @since May 8, 2021
 */
@Service
public class PdmstServiceImpl extends AbstractGenericService implements PdmstService {

	@Autowired private PdmstMapper pdmstMapper;

	@Override
	@Transactional
	public long save(Pdmst pdmst) {
		if(pdmst == null || StringUtils.isBlank(pdmst.getXtypetrn()) || StringUtils.isBlank(pdmst.getXtrn())) return 0;
		pdmst.setZid(sessionManager.getBusinessId());
		pdmst.setZauserid(getAuditUser());
		return pdmstMapper.save(pdmst);
	}

	@Override
	public long update(Pdmst pdmst) {
		if(pdmst == null || StringUtils.isBlank(pdmst.getXtypetrn())) return 0;
		pdmst.setZid(sessionManager.getBusinessId());
		pdmst.setZuuserid(getAuditUser());
		return pdmstMapper.update(pdmst);
	}

	@Override
	public List<Pdmst> getAll(Boolean zactive) {
		return pdmstMapper.getAllPdmst(sessionManager.getBusinessId(), zactive);
	}

	@Override
	public Pdmst findByXstaff(String xstaff, Boolean zactive) {
		if(StringUtils.isBlank(xstaff)) return null;
		return pdmstMapper.findPdmstByXstaff(xstaff, sessionManager.getBusinessId(), zactive);
	}

	@Override
	public List<Pdmst> searchStaff(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return pdmstMapper.searchStaff(hint.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<Pdmst> getAllHRPdmst(Boolean zactive) {
		return pdmstMapper.getAllPdmst(sessionManager.getBusinessId(), zactive);
	}
}
