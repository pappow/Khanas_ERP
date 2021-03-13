package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Arhed;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.ArhedMapper;
import com.asl.service.ArhedService;

@Service
public class ArhedServiceImpl extends AbstractGenericService implements ArhedService{
	
	@Autowired
	private ArhedMapper arhedMapper;

	@Override
	public long save(Arhed arhed) {
		if (arhed == null || StringUtils.isBlank(arhed.getXtype())
				|| StringUtils.isBlank(arhed.getXtrnarhed()))
			return 0;
		arhed.setZid(sessionManager.getBusinessId());
		return arhedMapper.saveArhed(arhed);
	}

	@Override
	public long update(Arhed arhed) {
		if (arhed == null || StringUtils.isBlank(arhed.getXvoucher()))
			return 0;
		arhed.setZid(sessionManager.getBusinessId());
		return arhedMapper.updateArhed(arhed);
	}

	@Override
	public Arhed findArhedByXvoucher(String xvoucher) {
		if (StringUtils.isBlank(xvoucher))
			return null;
		return arhedMapper.findArhedByXvoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllArheds() {
		
		return arhedMapper.getAllArhed(sessionManager.getBusinessId());
	}

	@Override
	public Arhed findObapByXCus(String xcus) {
		if(StringUtils.isBlank(xcus))
			return null;
		
		return arhedMapper.findObapByXCus(xcus, TransactionCodeType.ACCOUNT_OBAP.getCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllObaps() {
		// TODO Auto-generated method stub
		return null;
	}

}
