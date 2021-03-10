package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Poorddetail;
import com.asl.entity.Poordheader;
import com.asl.mapper.PoordMapper;
import com.asl.service.PoordService;

@Service
public class PoordServiceImpl extends AbstractGenericService implements PoordService {

	@Autowired
	private PoordMapper poordMapper;

	@Override
	public long save(Poordheader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXtype())
				|| StringUtils.isBlank(poordHeader.getXtrnpor()))
			return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		return poordMapper.savePoordHeader(poordHeader);
	}

	@Override
	public long update(Poordheader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum()))
			return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		return poordMapper.updatePoordHeader(poordHeader);
	}

	@Override
	public List<Poordheader> getAllPoordHeaders() {
		return poordMapper.getAllPoordHeader(sessionManager.getBusinessId());
	}

	@Override
	public Poordheader findPoordHeaderByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum))
			return null;

		return poordMapper.findPoordHeaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public Poordheader findBranchPoordHeaderByXpornumForCentral(String xpornum) {
		if (StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findBranchPoordHeaderByXpornumForCentral(xpornum, sessionManager.getZbusiness().getCentralzid());
	}

	@Override
	public long saveDetail(Poorddetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		long count = poordMapper.savePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	public long updatePoordHeaderTotalAmt(Poorddetail poordDetail) {
		if(poordDetail == null) return 0;
		return poordMapper.updatePoordHeaderTotalAmt(poordDetail);
	}

	@Override
	public long updateDetail(Poorddetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		long count = poordMapper.updatePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	public long deleteDetail(Poorddetail poordDetail) {
		if(poordDetail == null) return 0;
		long count = poordMapper.deletePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	public long archiveAllPoordDetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.archiveAllPoordDetailByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public long countOfRequisitionDetailsByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.countOfRequisitionDetailsByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public Poorddetail findPoorddetailByXportNumAndXrow(String xpornum, int xrow) {
		if(StringUtils.isBlank(xpornum) || xrow == 0) return null;
		return poordMapper.findPoorddetailByXportNumAndXrow(xpornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<Poorddetail> findPoorddetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return Collections.emptyList();
		return poordMapper.findPoorddetailByXpornum(xpornum, sessionManager.getBusinessId(), sessionManager.getZbusiness().getCentralzid());
	}

	@Override
	public List<Poordheader> getPoordHeadersByXtype(String xtype) {
		if(StringUtils.isBlank(xtype)) return Collections.emptyList();
		return poordMapper.getPoordHeadersByXtype(xtype, sessionManager.getBusinessId());
	}

	@Override
	public Poorddetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem) {
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(xitem)) return null;
		return poordMapper.findPoorddetailByXpornumAndXitem(xpornum, xitem, sessionManager.getBusinessId());
	}
}
