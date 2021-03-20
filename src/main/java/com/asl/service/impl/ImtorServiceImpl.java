package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.mapper.ImtorMapper;
import com.asl.service.ImtorService;

@Service
public class ImtorServiceImpl extends AbstractGenericService implements ImtorService {
	
	@Autowired
	private ImtorMapper imtorMapper;

	@Override
	public long save(ImtorHeader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtrntor()))
			return 0;
		imtorHeader.setZid(sessionManager.getBusinessId());
		return imtorMapper.saveImtorHeader(imtorHeader);
	}

	@Override
	public long update(ImtorHeader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtornum())) return 0;
		if(StringUtils.isBlank(imtorHeader.getZid())) imtorHeader.setZid(sessionManager.getBusinessId());
		return imtorMapper.updateImtorHeader(imtorHeader);
	}

	@Override
	public long saveDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum())) return 0;
		imtorDetail.setZid(sessionManager.getBusinessId());
		long count = imtorMapper.saveImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	public long updateDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum())) return 0;
		imtorDetail.setZid(sessionManager.getBusinessId());
		long count = imtorMapper.updateImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	public long deleteDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null) return 0;
		long count = imtorMapper.deleteImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	public ImtorHeader findImtorHeaderByXtornum(String xtornum) {
		if (StringUtils.isBlank(xtornum))
			return null;

		return imtorMapper.findImtorHeaderByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorHeader> getAllImtorHeader() {
		return imtorMapper.getAllImtorHeader(sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorDetail> findImtorDetailByXtornum(String xtornum) {
		if(StringUtils.isBlank(xtornum))
			return null;
		return imtorMapper.findImtorDetailByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXrow(String xtornum, int xrow) {
		if (StringUtils.isBlank(xtornum) || xrow == 0)
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXrow(xtornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXitem(String xtornum, String xitem) {
		if (StringUtils.isBlank(xtornum) || StringUtils.isBlank(xitem))
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXitem(xtornum, xitem, sessionManager.getBusinessId());
	}
	
	@Override
	public long updateImtorHeaderTotalAmt(ImtorDetail imtorDetail) {
		if(imtorDetail == null) return 0;
		return imtorMapper.updateImtorHeaderTotalAmt(imtorDetail);
	}

	@Override
	public void procConfirmTO(String xtornum, String p_action, String p_seq) {
		imtorMapper.procConfirmTO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xtornum, p_action, p_seq);
	}

}
