package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.mapper.PoordMapper;
import com.asl.service.PoordService;

@Service
public class PoordServiceImpl extends AbstractGenericService implements PoordService {

	@Autowired
	private PoordMapper poordMapper;

	@Override
	public long save(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXtype())
				|| StringUtils.isBlank(poordHeader.getXtrnpor()))
			return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		return poordMapper.savePoordHeader(poordHeader);
	}

	@Override
	public long update(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum()))
			return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		return poordMapper.updatePoordHeader(poordHeader);
	}

	@Override
	public List<PoordHeader> getAllPoordHeaders() {
		return poordMapper.getAllPoordHeader(sessionManager.getBusinessId());
	}

	@Override
	public PoordHeader findPoordHeaderByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum))
			return null;

		return poordMapper.findPoordHeaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public long saveDetail(PoordDetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		long count = poordMapper.savePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail) {
		if(poordDetail == null) return 0;
		return poordMapper.updatePoordHeaderTotalAmt(poordDetail);
	}

	@Override
	public long updateDetail(PoordDetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		long count = poordMapper.updatePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	public PoordDetail findPoorddetailByXportNumAndXrow(String xpornum, int xrow) {
		if(StringUtils.isBlank(xpornum) || xrow == 0) return null;
		return poordMapper.findPoorddetailByXportNumAndXrow(xpornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<PoordDetail> findPoorddetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return Collections.emptyList();
		return poordMapper.findPoorddetailByXpornum(xpornum, sessionManager.getBusinessId());
	}

}
