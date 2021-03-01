package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.PoordHeader;
import com.asl.mapper.PoordMapper;
import com.asl.service.PoordService;

@Service
public class PoordServiceImpl extends AbstractGenericService implements PoordService {
	
	@Autowired
	private PoordMapper poordMapper;

	@Override
	public long save(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum())) return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		return poordMapper.savePoordHeader(poordHeader);
	}

	@Override
	public long update(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum())) return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		return poordMapper.updatePoordHeader(poordHeader);
	}

	@Override
	public List<PoordHeader> getAllPoordHeaders() {
		return poordMapper.getAllPoordHeader(sessionManager.getBusinessId());
	}

	@Override
	public PoordHeader findPoordHeaderByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum)) return null;

		return poordMapper.findPoordHeaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

}
