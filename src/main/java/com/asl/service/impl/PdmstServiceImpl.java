package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Pdeducation;
import com.asl.entity.Pdexperience;
import com.asl.entity.Pdmst;
import com.asl.entity.Pdpromodt;
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
	public long delete(Pdmst pdmst) {
		if(pdmst == null) return 0;
		long count = pdmstMapper.delete(pdmst);
		return count;
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
	public List<Pdmst> getAllHRPdmst() {
		return pdmstMapper.getAllHRPdmst(sessionManager.getBusinessId());
	}

	@Override
	public Pdmst findAllPdmst(String xstaff) {
		if(StringUtils.isBlank(xstaff)) return null;
		return pdmstMapper.findAllPdmst(xstaff, sessionManager.getBusinessId());
	}

	//for HRQualification
	@Transactional
	@Override
	public long savePdeducation(Pdeducation pdqua) {
		if (pdqua == null)
			return 0;
		pdqua.setZid(sessionManager.getBusinessId());
		pdqua.setZauserid(getAuditUser());
		return pdmstMapper.savePdeducation(pdqua);
	}

	@Transactional
	@Override
	public long updatePdeducation(Pdeducation pdqua) {
		if (pdqua == null)
			return 0;
		pdqua.setZid(sessionManager.getBusinessId());
		pdqua.setZauserid(getAuditUser());
		return pdmstMapper.updatePdeducation(pdqua);
	}

	@Override
	public long deletePdeducation(Pdeducation pdqua) {
		if(pdqua == null) return 0;
		long count = pdmstMapper.deletePdeducation(pdqua);
		return count;
	}

	@Override
	public List<Pdeducation> getAllPdeducation() {
		return pdmstMapper.getAllPdeducation(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdeducation> findByPdeducation(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdeducation(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdeducation findPdeducationByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdeducationByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}
	
	//for HRExperience
	
	@Transactional
	@Override
	public long savePdexperience(Pdexperience pdex) {
		if (pdex == null)
			return 0;
		pdex.setZid(sessionManager.getBusinessId());
		pdex.setZauserid(getAuditUser());
		return pdmstMapper.savePdexperience(pdex);
	}

	@Transactional
	@Override
	public long updatePdexperience(Pdexperience pdex) {
		if (pdex == null)
			return 0;
		pdex.setZid(sessionManager.getBusinessId());
		pdex.setZauserid(getAuditUser());
		return pdmstMapper.updatePdexperience(pdex);
	}

	@Override
	public long deletePdexperience(Pdexperience pdex) {
		if(pdex == null) return 0;
		long count = pdmstMapper.deletePdexperience(pdex);
		return count;
	}

	@Override
	public List<Pdexperience> getAllPdexperience() {
		return pdmstMapper.getAllPdexperience(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdexperience> findByPdexperience(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdexperience(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdexperience findPdexperienceByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdexperienceByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}

	//for HRQualification
	@Transactional
	@Override
	public long savePdpromodt(Pdpromodt pdpr) {
		if (pdpr == null)
			return 0;
		pdpr.setZid(sessionManager.getBusinessId());
		pdpr.setZauserid(getAuditUser());
		return pdmstMapper.savePdpromodt(pdpr);
	}

	@Transactional
	@Override
	public long updatePdpromodt(Pdpromodt pdpr) {
		if (pdpr == null)
			return 0;
		pdpr.setZid(sessionManager.getBusinessId());
		pdpr.setZauserid(getAuditUser());
		return pdmstMapper.updatePdpromodt(pdpr);
	}

	@Override
	public long deletePdpromodt(Pdpromodt pdpr) {
		if(pdpr == null) return 0;
		long count = pdmstMapper.deletePdpromodt(pdpr);
		return count;
	}

	@Override
	public List<Pdpromodt> getAllPdpromodt() {
		return pdmstMapper.getAllPdpromodt(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdpromodt> findByPdpromodt(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdpromodt(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdpromodt findPdpromodtByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdpromodtByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}

	

	
}
