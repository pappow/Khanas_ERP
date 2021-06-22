package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Arhed;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.ArhedMapper;
import com.asl.service.ArhedService;

@Service
public class ArhedServiceImpl extends AbstractGenericService implements ArhedService{
	
	@Autowired
	private ArhedMapper arhedMapper;

	@Transactional
	@Override
	public long save(Arhed arhed) {
		if (arhed == null || StringUtils.isBlank(arhed.getXtype()) || StringUtils.isBlank(arhed.getXtrnarhed())) return 0;
		arhed.setZid(sessionManager.getBusinessId());
		arhed.setZauserid(getAuditUser());
		arhed.setXstaff(getAuditUser());
		return arhedMapper.saveArhed(arhed);
	}

	@Transactional
	@Override
	public long update(Arhed arhed) {
		if (arhed == null || StringUtils.isBlank(arhed.getXvoucher())) return 0;
		arhed.setZid(sessionManager.getBusinessId());
		arhed.setZuuserid(getAuditUser());
		arhed.setXstaff(getAuditUser());
		return arhedMapper.updateArhed(arhed);
	}

	@Override
	public Arhed findArhedByXvoucher(String xvoucher) {
		if (StringUtils.isBlank(xvoucher)) return null;
		return arhedMapper.findArhedByXvoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllArheds() {
		return arhedMapper.getAllArhed(sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllArhedByXtrn(String xtrn) {
		if(StringUtils.isBlank(xtrn)) return null;
		return arhedMapper.getAllArhedByXtrn(xtrn, sessionManager.getBusinessId());
	}

	@Override
	public Arhed findObapByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return arhedMapper.findObapByXcus(xcus, TransactionCodeType.ACCOUNT_OBAP.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllObaps() {
		return arhedMapper.getAllObaps(TransactionCodeType.ACCOUNT_OBAP.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public Arhed findAdapByXcus(String xcus) {
		if(StringUtils.isBlank(xcus))
			return null;		
		return arhedMapper.findAdapByXcus(xcus, TransactionCodeType.ACCOUNT_ADAP.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllAdaps() {
		return arhedMapper.getAllAdaps(TransactionCodeType.ACCOUNT_ADAP.getdefaultCode(), sessionManager.getBusinessId());
	}	
	
	@Override
	public Arhed findAdarByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return arhedMapper.findAdarByXcus(xcus, TransactionCodeType.ACCOUNT_ADAR.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllAdars() {
		return arhedMapper.getAllAdars(TransactionCodeType.ACCOUNT_ADAR.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> findByXstaff(String xstaff){
		if(StringUtils.isBlank(xstaff)) return Collections.emptyList();
		return arhedMapper.findByXstaff(xstaff, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteVoucher(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return 0;
		return arhedMapper.deleteVoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public List<Arhed> getAllArhedByXtrnarhedAndXtype(String xtrnarhed, String xtype) {
		if(StringUtils.isBlank(xtrnarhed) || StringUtils.isBlank(xtype)) return Collections.emptyList();
		return arhedMapper.getAllArhedByXtrnarhedAndXtype(xtrnarhed, xtype, sessionManager.getBusinessId());
	}

	
}
