package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(propagation = Propagation.REQUIRED)
	public long saveBatchMoHeader(List<Moheader> moheaders) {
		if(moheaders == null || moheaders.isEmpty()) return 0;
		long count = 0;
		for(Moheader moh : moheaders) {
			count += saveMoHeader(moh);
		}
		return count;
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
		if(StringUtils.isBlank(xbatch)) return Collections.emptyList();
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

	@Override
	public Modetail findModetailByXbatchAndXitem(String xbatch, String xitem) {
		if(StringUtils.isBlank(xbatch) || StringUtils.isBlank(xitem)) return null;
		return moMapper.findModetailByXbatchAndXitem(xbatch, xitem, sessionManager.getBusinessId());
	}

	@Override
	public void processProduction(String batch, String action, String errseq) {
		moMapper.processProduction(batch, action, errseq, sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername());
	}

	

}
