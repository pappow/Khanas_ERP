package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.mapper.OpdoMapper;
import com.asl.service.OpdoService;

@Service
public class OpdoServiceImpl extends AbstractGenericService implements OpdoService {
	
	@Autowired
	private OpdoMapper opdoMapper;

	@Override
	public long save(Opdoheader opdoHeader) {
		if (opdoHeader == null || StringUtils.isBlank(opdoHeader.getXtype()))
			return 0;
		opdoHeader.setZid(sessionManager.getBusinessId());
		return opdoMapper.saveOpdoHeader(opdoHeader);
	}

	@Override
	public long update(Opdoheader opdoHeader) {
		if (opdoHeader == null || StringUtils.isBlank(opdoHeader.getXdornum())) 
			return 0;
		if(StringUtils.isBlank(opdoHeader.getZid()))
			opdoHeader.setZid(sessionManager.getBusinessId());
		return opdoMapper.updateOpdoHeader(opdoHeader);
	}

	@Override
	public long saveDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) return 0;
		opdoDetail.setZid(sessionManager.getBusinessId());
		long count = opdoMapper.saveOpdoDetail(opdoDetail);
		//if(count != 0) { count = updateOpdoHeaderTotalAmt(opdoDetail); }
		 
		return count;
	}

	@Override
	public long updateDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) return 0;
		opdoDetail.setZid(sessionManager.getBusinessId());
		long count = opdoMapper.updateOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmt(opdoDetail); }
		return count;
	}

	@Override
	public long deleteDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null) return 0;
		long count = opdoMapper.deleteOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmt(opdoDetail); }
		return count;
	}

	@Override
	public Opdoheader findOpdoHeaderByXdornum(String xdornum) {
		if (StringUtils.isBlank(xdornum))
			return null;

		return opdoMapper.findOpdoHeaderByXdornum(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> getAllOpdoHeader() {
		return opdoMapper.getAllOpdoHeader(sessionManager.getBusinessId());
	}

	@Override
	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow) {
		if(StringUtils.isBlank(xdornum) || xrow == 0) return null;
		return opdoMapper.findOpdoDetailByXdornumAndXrow(xdornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum) {
		if(StringUtils.isBlank(xdornum))
			return Collections.emptyList();
		return opdoMapper.findOpdoDetailByXdornum(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public long updateOpdoHeaderTotalAmt(Opdodetail opdoDetail) {
		if(opdoDetail == null) return 0;
		return opdoMapper.updateOpdoHeaderTotalAmt(opdoDetail);
	}

}
