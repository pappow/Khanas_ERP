package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Modetail;
import com.asl.entity.Moheader;
import com.asl.mapper.MoMapper;
import com.asl.service.MoService;

/**
 * @author Zubayer Ahamed
 * @since Mar 18, 2021
 */
@Service
public class MoServiceImpl extends AbstractGenericService implements MoService {

	@Autowired private MoMapper moMapper;

	@Override
	public long saveMoHeader(Moheader moheader) {
		if(moheader == null || StringUtils.isBlank(moheader.getXtypetrn())) return 0;
		moheader.setZid(sessionManager.getBusinessId());
		return moMapper.saveMoHeader(moheader);
	}

	@Override
	public long updateMoHeader(Moheader moheader) {
		if(moheader == null || StringUtils.isBlank(moheader.getXbatch())) return 0;
		moheader.setZid(sessionManager.getBusinessId());
		return moMapper.updateMoHeader(moheader);
	}

	@Override
	public long saveMoDetail(Modetail modetail) {
		if(modetail == null || StringUtils.isBlank(modetail.getXbatch())) return 0;
		modetail.setZid(sessionManager.getBusinessId());
		return moMapper.saveMoDetail(modetail);
	}

	@Override
	public long updateMoDetail(Modetail modetail) {
		if(modetail == null || StringUtils.isBlank(modetail.getXbatch()) || modetail.getXrow() == 0) return 0;
		modetail.setZid(sessionManager.getBusinessId());
		return moMapper.updateMoDetail(modetail);
	}

	@Override
	public Moheader findMoHeaderByXbatch(String xbatch) {
		if(StringUtils.isBlank(xbatch)) return null;
		return moMapper.findMoHeaderByXbatch(xbatch, sessionManager.getBusinessId());
	}

	@Override
	public Modetail findModetailByXrowAndXbatch(int xrow, String xbatch) {
		if(StringUtils.isBlank(xbatch) || xrow == 0) return null;
		return moMapper.findModetailByXrowAndXbatch(xrow, xbatch, sessionManager.getBusinessId());
	}

	@Override
	public List<Modetail> findModetailByXbatch(String xbatch) {
		if(StringUtils.isBlank(xbatch)) return null;
		return moMapper.findModetailByXbatch(xbatch, sessionManager.getBusinessId());
	}

	@Override
	public List<Moheader> getAllMoheader() {
		return moMapper.getAllMoheader(sessionManager.getBusinessId());
	}

	@Override
	public Moheader findMoheaderByXchalanAndXitem(String xchalan, String xitem) {
		if(StringUtils.isBlank(xchalan) || StringUtils.isBlank(xitem)) return null;
		return moMapper.findMoheaderByXchalanAndXitem(xchalan, xitem, sessionManager.getBusinessId());
	}

	

}
