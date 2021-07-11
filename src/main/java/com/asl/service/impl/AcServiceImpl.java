package com.asl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;
import com.asl.mapper.AcMapper;
import com.asl.service.AcService;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2021
 */
@Service
public class AcServiceImpl extends AbstractGenericService implements AcService {

	@Autowired private AcMapper acMapper;

	@Transactional
	@Override
	public long saveAcheader(Acheader acheader) {
		if(acheader == null) return 0;
		acheader.setZid(sessionManager.getBusinessId());
		acheader.setZauserid(getAuditUser());
		return acMapper.saveAcheader(acheader);
	}

	@Transactional
	@Override
	public long updateAcheader(Acheader acheader) {
		if(acheader == null) return 0;
		acheader.setZid(sessionManager.getBusinessId());
		acheader.setZuuserid(getAuditUser());
		return acMapper.updateAcheader(acheader);
	}

	@Override
	public List<Acheader> getAllAcheader() {
		return acMapper.getAllAcheader(sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long saveAcdetail(Acdetail acdetail) {
		if(acdetail == null) return 0;
		acdetail.setZid(sessionManager.getBusinessId());
		acdetail.setZauserid(getAuditUser());
		return acMapper.saveAcdetail(acdetail);
	}

	@Transactional
	@Override
	public long updateAcdetail(Acdetail acdetail) {
		if(acdetail == null) return 0;
		acdetail.setZid(sessionManager.getBusinessId());
		acdetail.setZuuserid(getAuditUser());
		return acMapper.updateAcdetail(acdetail);
	}

	@Override
	public List<Acdetail> getAllAcdetail() {
		return acMapper.getAllAcdetail(sessionManager.getBusinessId());
	}

}
