package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Cacus;
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
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum())) return 0;
		if(StringUtils.isBlank(poordHeader.getZid())) poordHeader.setZid(sessionManager.getBusinessId());
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
	public PoordHeader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid) {
		if (StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findBranchPoordHeaderByXpornumForCentral(xpornum, branchzid);
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
	public long deleteDetail(PoordDetail poordDetail) {
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
	public PoordDetail findPoorddetailByXportNumAndXrow(String xpornum, int xrow) {
		if(StringUtils.isBlank(xpornum) || xrow == 0) return null;
		return poordMapper.findPoorddetailByXportNumAndXrow(xpornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<PoordDetail> findPoorddetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return Collections.emptyList();
		return poordMapper.findPoorddetailByXpornum(xpornum, sessionManager.getBusinessId(), sessionManager.getZbusiness().getCentralzid());
	}

	@Override
	public List<PoordDetail> findPoordDetailsByXpornumAndBranchZid(String xpornum, String branchzid) {
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(branchzid)) return Collections.emptyList();
		return poordMapper.findPoordDetailsByXpornumAndBranchZid(xpornum, branchzid);
	}

	@Override
	public List<PoordHeader> getPoordHeadersByXtype(String xtype) {
		if(StringUtils.isBlank(xtype)) return Collections.emptyList();
		return poordMapper.getPoordHeadersByXtype(xtype, sessionManager.getBusinessId());
	}

	@Override
	public PoordDetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem) {
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(xitem)) return null;
		return poordMapper.findPoorddetailByXpornumAndXitem(xpornum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public Cacus findBranchCustomerByRequsitionNumber(String xpornum, String branchid) {
		if(StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findBranchCustomerByRequsitionNumber(xpornum, branchid, sessionManager.getBusinessId());
	}

	
	@Override
	public List<PoordHeader> searchXpornum(String xpornum){
		return poordMapper.searchXpornum(xpornum.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public Cacus findBranchCustomerByRequsitionNumber(String xpornum) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
