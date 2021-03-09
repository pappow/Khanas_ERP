package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.mapper.OpordMapper;
import com.asl.service.OpordService;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Service
public class OpordServiceImpl extends AbstractGenericService implements OpordService {

	@Autowired private OpordMapper opordMapper;

	@Override
	public long saveOpordHeader(Opordheader opordheader) {
		if(opordheader == null) return 0;
		opordheader.setZid(sessionManager.getBusinessId());
		return opordMapper.saveOpordHeader(opordheader);
	}

	@Override
	public long updateOpordHeader(Opordheader opordheader) {
		if(opordheader == null) return 0;
		return opordMapper.updateOpordHeader(opordheader);
	}

	@Override
	public long saveOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		oporddetail.setZid(sessionManager.getBusinessId());
		return opordMapper.saveOpordDetail(oporddetail);
	}

	@Override
	public long updateOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		return opordMapper.updateOpordDetail(oporddetail);
	}

	@Override
	public Opordheader findOpordHeaderByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return null;
		return opordMapper.findOpordHeaderByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow) {
		if(StringUtils.isBlank(xordernum) || xrow == 0) return null;
		return opordMapper.findOporddetailByXordernumAndXrow(xordernum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<Oporddetail> findOporddetailByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return null;
		return opordMapper.findOporddetailByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> getAllOpordheader() {
		return opordMapper.getAllOpordheader(sessionManager.getBusinessId());
	}

	
}
